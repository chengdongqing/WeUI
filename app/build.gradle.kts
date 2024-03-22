plugins {
    alias(libs.plugins.weui.android.compose.application)
}

android {
    namespace = "top.chengdongqing.weui"

    defaultConfig {
        applicationId = "top.chengdongqing.weui"
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        android {
            defaultConfig {
                ndk {
                    abiFilters.addAll(listOf("arm64-v8a")) // 暂仅支持64位ARM架构的设备（32位：armeabi-v7a）
                }
            }
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

    implementation(project(":core:ui:theme"))
    implementation(project(":core:ui:components"))
    implementation(project(":core:utils"))
    implementation(project(":feature:basic"))
    implementation(project(":feature:form"))
    implementation(project(":feature:feedback"))
    implementation(project(":feature:media"))
    implementation(project(":feature:network"))
    implementation(project(":feature:charts"))
    implementation(project(":feature:hardware"))
    implementation(project(":feature:location"))
    implementation(project(":feature:qrcode"))
    implementation(project(":feature:system"))
    implementation(project(":feature:demos"))
}