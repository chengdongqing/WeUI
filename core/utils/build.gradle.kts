plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.core.utils"
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.pinyin)

    implementation(projects.core.data.model)
    implementation(projects.core.ui.theme)
}