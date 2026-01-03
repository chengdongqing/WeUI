plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.basic"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.bundles.coil)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:data:model"))
    implementation(project(":core:utils"))
}