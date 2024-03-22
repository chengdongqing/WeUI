plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.demos"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.reorderable)
    implementation(libs.lunar)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
    implementation(project(":feature:demos:gallery"))
    implementation(project(":feature:demos:file-browser"))
    implementation(project(":feature:demos:video-channel"))
    implementation(project(":feature:demos:image-cropper"))
    implementation(project(":feature:demos:paint"))
}