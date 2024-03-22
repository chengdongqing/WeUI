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
    implementation(fileTree("libs") { include("*.jar") })
    implementation(libs.amap)
    implementation(libs.amap.search)

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
}