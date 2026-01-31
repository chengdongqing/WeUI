plugins {
    alias(libs.plugins.weui.android.compose.application)
}

android {
    namespace = "top.chengdongqing.weui"
    defaultConfig {
        applicationId = "top.chengdongqing.weui"
        targetSdk = 36
        versionCode = 20260101
        versionName = "2026.01.01"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            abiFilters.addAll(listOf("arm64-v8a"))
        }
    }

    buildTypes {
        /*debug {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }*/
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // 支持多版本构建
    buildFeatures {
        buildConfig = true
    }
    flavorDimensions.add("version")
    productFlavors {
        create("lite") {
            dimension = "version"
            applicationIdSuffix = ".lite"
            resValue("string", "app_name", "WeUI Lite")
        }
        create("full") {
            dimension = "version"
            resValue("string", "app_name", "WeUI")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.navigation.compose)

    implementation(projects.core.ui.theme)
    implementation(projects.core.ui.components)
    implementation(projects.core.utils)
    implementation(projects.feature.basic)
    implementation(projects.feature.form)
    implementation(projects.feature.media)
    implementation(projects.feature.feedback)
    implementation(projects.feature.system)
    implementation(projects.feature.network)
    implementation(projects.feature.hardware)
    implementation(projects.feature.charts)
    implementation(projects.feature.qrcode)
    implementation(projects.feature.samples)

    // 仅全量版包含地图模块
    "fullImplementation"(projects.feature.location)

    // // 先运行 ./gradlew publishToMavenLocal
    // // 然后可以使用本地 Maven 发布的库
    // val weuiVersion = "2026.01.01"
    // implementation("top.chengdongqing.weui:core-ui-theme:$weuiVersion")
    // implementation("top.chengdongqing.weui:core-ui-components:$weuiVersion")
    // implementation("top.chengdongqing.weui:core-utils:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-basic:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-form:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-media:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-feedback:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-system:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-network:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-hardware:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-charts:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-qrcode:$weuiVersion")
    // implementation("top.chengdongqing.weui:feature-samples:$weuiVersion")

    // "fullImplementation"("top.chengdongqing.weui:feature-location:$weuiVersion")
}