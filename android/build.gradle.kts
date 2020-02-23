plugins {
    `android-application`
    kotlin("android")
    kotlin("android.extensions")
}

android {
    defaultConfig {
        applicationId = Android.applicationId
        versionCode = Android.versionCode
        versionName = Android.versionName
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":shared:data"))
    implementation(project(":shared:repository"))

    implementation(Libraries.Kotlin.android)

    implementation(Libraries.Coroutines.android)

    implementation(Libraries.Ktor.clientAndroid)
    implementation(Libraries.Ktor.jsonAndroid)

    implementation(Libraries.Serialization.android)

    implementation(Libraries.AndroidX.appCompat)
    implementation(Libraries.AndroidX.coreKtx)
    implementation(Libraries.AndroidX.constraintLayout)
    implementation(Libraries.AndroidX.recyclerView)
    implementation(Libraries.AndroidX.lifecycleViewModel)
    implementation(Libraries.AndroidX.lifecycleLiveData)
}
