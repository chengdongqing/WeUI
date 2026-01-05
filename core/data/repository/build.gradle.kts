plugins {
    alias(libs.plugins.weui.android.compose.library)
}

android {
    namespace = "top.chengdongqing.core.data.repository"
}

dependencies {
    implementation(projects.core.data.model)
}