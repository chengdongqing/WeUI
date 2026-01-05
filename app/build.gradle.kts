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
    implementation(projects.feature.feedback)
    implementation(projects.feature.media)
    implementation(projects.feature.network)
    implementation(projects.feature.charts)
    implementation(projects.feature.hardware)
    implementation(projects.feature.location)
    implementation(projects.feature.qrcode)
    implementation(projects.feature.system)
    implementation(projects.feature.samples)
}