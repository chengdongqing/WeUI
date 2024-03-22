plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.demos.filebrowser"
}

dependencies {
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.accompanist.permissions)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
}