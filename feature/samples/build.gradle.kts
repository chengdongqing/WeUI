plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.samples"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.bundles.coil)
    implementation(libs.lunar)

    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.utils)
    implementation(projects.feature.samples.fileBrowser)
    implementation(projects.feature.samples.videoChannel)
    implementation(projects.feature.samples.paint)
    implementation(libs.androidx.foundation)
}