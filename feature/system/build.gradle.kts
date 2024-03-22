plugins {
    alias(libs.plugins.weui.android.compose.library)
    alias(libs.plugins.weui.android.room)
}

android {
    namespace = "top.chengdongqing.weui.feature.system"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.accompanist.permissions)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
}