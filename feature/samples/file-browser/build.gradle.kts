plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.samples.filebrowser"
}

dependencies {
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.navigation.compose)
    implementation(libs.bundles.coil)

    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.data.model)
    implementation(projects.core.utils)
}