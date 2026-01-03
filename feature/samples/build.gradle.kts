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

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
    implementation(project(":feature:samples:file-browser"))
    implementation(project(":feature:samples:video-channel"))
    implementation(project(":feature:samples:paint"))
    implementation(libs.androidx.foundation)
}