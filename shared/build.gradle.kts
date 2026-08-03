import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0"
        summary = "Shared module for WeUI-KMP"
        homepage = "https://github.com/chengdongqing/WeUI"
        ios.deploymentTarget = "18.2"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "Shared"
            isStatic = true
            binaryOption("bundleId", "top.chengdongqing.shared")
        }

        pod("LunarSwift") {
            version = "1.1.8"
        }
    }

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    android {
        namespace = "top.chengdongqing.weui.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.coil.video)
            implementation(libs.pinyin)
            implementation(libs.coil.network.okhttp)
            implementation(libs.lunar)
            implementation(libs.accompanist.permissions)
        }
        iosMain.dependencies {
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.coil.network.okhttp)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.pinyin)
            implementation(libs.lunar)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.adaptive)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.lifecycle.viewmodel.navigation3)

            implementation(libs.kotlinx.datetime)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.navigation.ui)
            implementation(libs.room.runtime)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
        }
        webMain.dependencies {
            implementation(libs.androidx.sqlite.web)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
    ksp(libs.room.compiler)
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

// Kotlin 2.4 currently raises synthetic Pod targets only to iOS 12, while Xcode 27
// no longer ships simulator support below iOS 15. Patch the generated Podfile so
// third-party pods inherit this module's deployment target after every generation.
val syntheticIosPodfile = layout.buildDirectory.file("cocoapods/synthetic/ios/Podfile")
val patchSyntheticIosPodfile = tasks.register<Exec>("patchSyntheticIosPodfile") {
    dependsOn("podGenIos")
    inputs.file(syntheticIosPodfile)
    outputs.upToDateWhen { false }
    commandLine(
        "/usr/bin/sed",
        "-i",
        "",
        "-e",
        """s/deployment_target_major < 12 || (deployment_target_major == 12 && deployment_target_minor < 0)/deployment_target_major < 15 || (deployment_target_major == 15 \&\& deployment_target_minor < 0)/g""",
        "-e",
        "s/#{12}.#{0}/#{15}.#{0}/g",
        syntheticIosPodfile.get().asFile.absolutePath
    )
}

tasks.named("podInstallSyntheticIos").configure {
    dependsOn(patchSyntheticIosPodfile)
}
