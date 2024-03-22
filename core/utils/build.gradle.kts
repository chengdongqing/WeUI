plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.core.utils"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.pinyin)
    implementation(libs.amap)
    implementation(libs.amap.search)

    implementation(project(":core:data:model"))
    implementation(project(":core:ui:theme"))
}