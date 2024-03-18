@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.devtools.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "top.chengdongqing.weui"
    compileSdk = 34

    defaultConfig {
        applicationId = "top.chengdongqing.weui"
        minSdk = 26
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

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose) // 支持页面间导航
    implementation(libs.accompanist.navigation.animation) // 导航支持动画
    implementation(libs.accompanist.permissions) // 简化权限请求
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons.extended) // 图标扩展
    implementation(libs.coil.compose) // 异步图片加载
    implementation(libs.androidx.room.runtime) // room运行时
    implementation(libs.androidx.room.ktx)  // room支持协程
    ksp(libs.androidx.room.compiler) // room生成接口实现
    implementation(libs.androidx.biometric) // 简化生物认证
    implementation(libs.androidx.camera.camera2) // 简化相机调用
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.mlkit.barcode.scanning) // 二维码扫描
    implementation(libs.zxing.core) // 二维码生成
    implementation(libs.lunar) // 农历计算库
    implementation(libs.pinyin) // 汉字转拼音
    implementation(libs.retrofit) // 网络请求
    implementation(libs.retrofit.converter)
    implementation(libs.reorderable) // 拖拽排序
    implementation(fileTree("libs") { include("*.jar") })
    implementation(libs.amap) // 高德地图
    implementation(libs.amap.search) // 地图支持搜索
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}