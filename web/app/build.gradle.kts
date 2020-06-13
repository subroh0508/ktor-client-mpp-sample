import java.io.File
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmDependency
import org.jetbrains.kotlin.gradle.targets.js.npm.npmProject
import org.jetbrains.kotlin.gradle.targets.js.npm.NpmProject

plugins {
    kotlin("js")
    id("kotlinx-serialization")
}

kotlin {
    target {
        compilations.all {
            compileKotlinTask.kotlinOptions {
                moduleKind = "commonjs"
                sourceMap = true
                sourceMapEmbedSources = null
            }
            if (compilationName == "main") {
                buildStorybookTask(npmProject)
            }
        }
        //useCommonJs()
        browser {
            // Ktor's known issue
            // Issue: https://github.com/ktorio/ktor/issues/1339
            // YouTrack: https://youtrack.jetbrains.com/issue/KT-36484
            dceTask {
                keep("ktor-ktor-io.\$\$importsForInline\$\$.ktor-ktor-io.io.ktor.utils.io")
            }
            runTask {
                sourceMaps = true
                devServer = KotlinWebpackConfig.DevServer(
                    port = 8088,
                    contentBase = listOf("${projectDir.path}/src/main/resources")
                )
                outputFileName = "bundle.js"
            }
            webpackTask {
                sourceMaps = false
                outputFileName = "bundle.js"
            }
        }
    }

    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":shared:utilities"))
                implementation(project(":shared:components:core"))
                implementation(project(":shared:model"))
                implementation(project(":shared:infra:repository"))

                implementation(Libraries.Kotlin.js)

                implementation(Libraries.Coroutines.js)

                implementation(Libraries.Ktor.clientJs)
                implementation(Libraries.Ktor.jsonJs)
                implementation(Libraries.Ktor.serializationJs)

                implementation(Libraries.Serialization.js)

                implementation(Libraries.Html.js)

                implementation(Libraries.JsWrappers.react)
                implementation(Libraries.JsWrappers.reactDom)
                implementation(Libraries.JsWrappers.reactRouterDom)
                implementation(Libraries.JsWrappers.css)
                implementation(Libraries.JsWrappers.styled)
                implementation(Libraries.JsWrappers.extensions)
                implementation(Libraries.JsWrappers.MaterialUi.core)

                implementation(Libraries.Kodein.js)

                implementation(npm(Libraries.Npm.react, Libraries.Npm.reactVersion))
                implementation(npm(Libraries.Npm.reactDom, Libraries.Npm.reactVersion))
                implementation(npm(Libraries.Npm.reactRouterDom, "^5.2.0"))
                implementation(npm(Libraries.Npm.styledComponent, Libraries.Npm.styledComponentVersion))
                implementation(npm(Libraries.Npm.inlineStylePrefixer, Libraries.Npm.inlineStylePrefixerVersion))
                implementation(npm(Libraries.Npm.abortController, Libraries.Npm.abortControllerVersion))
                implementation(npm(Libraries.Npm.textEncoding, Libraries.Npm.textEncodingVersion))
                implementation(npm(Libraries.Npm.I18next.core, Libraries.Npm.I18next.version))
                implementation(npm(Libraries.Npm.I18next.httpBackend, Libraries.Npm.I18next.httpBackendVersion))
                implementation(npm(Libraries.Npm.I18next.react, Libraries.Npm.I18next.reactVersion))

                implementation(devNpm("html-webpack-plugin", "^3.2.0"))
                implementation(devNpm("webpack-cdn-plugin", "^3.2.2"))
                implementation(devNpm("@storybook/react", "^5.3.19"))
            }
        }
    }
}


val browserWebpack = tasks.getByName("browserProductionWebpack")

val copyDistributions by tasks.registering {
    doLast {
        copy {
            val destinationDir = File("$rootDir/public")
            if (!destinationDir.exists()) {
                destinationDir.mkdir()
            }
            val distributions = File("$buildDir/distributions/")
            from(distributions)
            into(destinationDir)
        }
    }
}

browserWebpack.finalizedBy(copyDistributions)

fun devNpm(name: String, version: String = "*") = NpmDependency(project, name, version, NpmDependency.Scope.DEV)

fun Project.buildStorybookTask(npmProject: NpmProject) {
    tasks.create("storybook", Exec::class) {
        doFirst {
            val dir = File(npmProject.dir, ".storybook")
            if (!dir.exists()) {
                dir.mkdir()
            }

            File(dir, "main.js").writer().use {
                it.appendln("""
                    module.exports = { stories: [] };
                """.trimIndent())
            }
        }

        workingDir = npmProject.dir
        commandLine("node_modules/.bin/start-storybook", "start-storybook")
    }
}
