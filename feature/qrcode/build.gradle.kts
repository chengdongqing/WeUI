plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.qrcode"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.mlkit.barcode.scanning)
    implementation(libs.bundles.camerax)
    implementation(libs.zxing.core)

    implementation(projects.core.data.model)
    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.utils)
}