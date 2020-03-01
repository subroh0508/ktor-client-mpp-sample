plugins {
    `android-library`
    kotlin("android")
    kotlin("android.extensions")
}

dependencies {
    implementation(Libraries.Kotlin.android)
    implementation(Libraries.Kotlin.reflect)

    implementation(Libraries.Coroutines.android)

    implementation(Libraries.AndroidX.appCompat)
    implementation(Libraries.AndroidX.coreKtx)
    implementation(Libraries.AndroidX.constraintLayout)
    implementation(Libraries.AndroidX.coordinatorLayout)
    implementation(Libraries.AndroidX.recyclerView)
    implementation(Libraries.AndroidX.lifecycleViewModel)
    implementation(Libraries.AndroidX.lifecycleLiveData)
    implementation(Libraries.AndroidX.material)

    api(Libraries.AndroidX.Navigation.runtimeKtx)
    api(Libraries.AndroidX.Navigation.fragmentKtx)
    api(Libraries.AndroidX.Navigation.uiKtx)
}