plugins {
    alias(libs.plugins.weui.android.compose.library)
    alias(libs.plugins.weui.android.room)
}

android {
    namespace = "top.chengdongqing.weui.feature.system"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.accompanist.permissions)

    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.utils)
    implementation(projects.core.data.model)
}