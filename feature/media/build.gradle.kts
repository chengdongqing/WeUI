plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.media"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.navigation.compose)
    implementation(libs.coil.compose)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
    implementation(project(":core:data:model"))
    implementation(project(":core:data:repository"))
}