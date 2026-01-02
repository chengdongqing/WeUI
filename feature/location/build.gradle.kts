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
    implementation(libs.bundles.datastore)
    implementation(files("${rootProject.projectDir}/libs/AMap3DMap_11.1.060_AMapSearch_9.7.4_AMapLocation_11.1.060_20251229.aar"))

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
}