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
    // implementation(files("${rootProject.projectDir}/libs/AMap3DMap_11.1.060_AMapSearch_9.7.4_AMapLocation_11.1.060_20251229.aar"))
    implementation("top.chengdongqing.amap:amap-all:11.1.060")

    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.utils)
}


// 发布高德地图本地 AAR 到 Maven
publishing {
    publications {
        create<MavenPublication>("amap") {
            groupId = "top.chengdongqing.amap"
            artifactId = "amap-all"
            version = "11.1.060"
            artifact("${rootProject.projectDir}/libs/AMap3DMap_11.1.060_AMapSearch_9.7.4_AMapLocation_11.1.060_20251229.aar")
        }
    }
    repositories {
        mavenLocal()
    }
}

// 强制编译前发布 amap 到本地 Maven
tasks.configureEach {
    if (name == "preBuild") {
        dependsOn("publishAmapPublicationToMavenLocal")
    }
}
