plugins {
    kotlin("multiplatform")
    `android-library`
}

kotlinMpp {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libraries.Ktor.client)
                implementation(Libraries.Ktor.clientMock)
                implementation(Libraries.Kotest.engine)
                implementation(Libraries.Kotest.assertion)
            }
        }
        val androidMain by getting
        val jsMain by getting
    }
}