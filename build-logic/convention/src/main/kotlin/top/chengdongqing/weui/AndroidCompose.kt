package top.chengdongqing.weui

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 24
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("compose-compiler").get().toString()
        }

        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            add("implementation", platform(bom))

            listOf(
                "core.ktx",
                "lifecycle.runtime.ktx",
                "activity.compose",
                "ui",
                "ui.graphics",
                "ui.tooling.preview",
                "material3",
                "material.icons.extended"
            ).forEach { alias ->
                add("implementation", libs.findLibrary(alias).get())
            }
            add("testImplementation", libs.findLibrary("junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx.test.ext.junit").get())
            add("androidTestImplementation", libs.findLibrary("espresso.core").get())
            add("androidTestImplementation", platform(bom))
            add("androidTestImplementation", libs.findLibrary("ui.test.junit4").get())
            add("debugImplementation", libs.findLibrary("ui.tooling").get())
            add("debugImplementation", libs.findLibrary("ui.test.manifest").get())
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
}