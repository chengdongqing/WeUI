plugins {
    alias(libs.plugins.weui.android.compose.library)
    id("kotlin-parcelize")
}

android {
    namespace = "top.chengdongqing.weui.feature.location"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.amap)
    implementation(libs.bundles.datastore)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
}