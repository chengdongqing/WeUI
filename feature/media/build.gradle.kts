plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.media"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.navigation.compose)
    implementation(libs.bundles.coil)

    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.utils)
    implementation(projects.core.data.model)
    implementation(projects.core.data.repository)
}