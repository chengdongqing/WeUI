plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.core.ui.components"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.bundles.camerax)
    implementation(libs.accompanist.permissions)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:data:model"))
    implementation(project(":core:data:repository"))
    implementation(project(":core:utils"))
}