plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.core.ui.components"
}

dependencies {
    implementation(libs.bundles.coil)
    implementation(libs.coil.zoomable)
    implementation(libs.bundles.camerax)
    implementation(libs.accompanist.permissions)

    implementation(projects.core.ui.theme)
    implementation(projects.core.data.model)
    implementation(projects.core.data.repository)
    implementation(projects.core.utils)
}