package top.chengdongqing.weui

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension
) {
    commonExtension.apply {
        compileSdk = 36

        defaultConfig.apply {
            minSdk = 24
        }
        compileOptions.apply {
            // 开启脱糖支持
            isCoreLibraryDesugaringEnabled = true

            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
        buildFeatures.apply {
            compose = true
            resValues = true
        }

        dependencies {
            val bom = libs.findLibrary("compose-bom").get()

            add("implementation", platform(bom))
            add("implementation", libs.findBundle("compose.ui").get())
            add("testImplementation", libs.findLibrary("junit").get())
            add("androidTestImplementation", platform(bom))
            add("androidTestImplementation", libs.findBundle("compose.test").get())
            add("debugImplementation", libs.findLibrary("ui.tooling").get())
            add("debugImplementation", libs.findLibrary("ui.test.manifest").get())
            add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}