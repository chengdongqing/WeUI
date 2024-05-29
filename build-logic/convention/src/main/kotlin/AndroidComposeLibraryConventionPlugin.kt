import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import top.chengdongqing.weui.configureAndroidCompose

class AndroidComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val extension = extensions.getByType<LibraryExtension>().apply {
                defaultConfig {
                    consumerProguardFile("consumer-rules.pro")
                }
            }
            configureAndroidCompose(extension)
        }
    }
}