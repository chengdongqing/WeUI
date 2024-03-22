plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.network"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.bundles.retrofit)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
    implementation(project(":core:data:model"))
}