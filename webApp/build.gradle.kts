import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                devtool = null
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)

            implementation(libs.compose.ui)

            implementation(npm("@js-joda/core", "6.0.1"))
            implementation(npm("@js-joda/timezone", "2.25.1"))
            implementation(npm("pinyin-pro", "3.28.1"))
            implementation(npm("sqlite-wasm-worker", project.file("sqlite-wasm-worker")))
        }
    }
}