plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.hardware"
}

dependencies {
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.navigation.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.biometric)

    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.utils)
}