plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.weui.feature.feedback"
}

dependencies {
    implementation(libs.navigation.compose)

    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.data.model)
    implementation(projects.core.utils)
}