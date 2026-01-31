import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.get

class AndroidMavenPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (target == target.rootProject) {
            // 在根项目应用时，配置所有符合条件的子项目
            target.subprojects {
                val subproject = this
                subproject.plugins.withId("com.android.library") {
                    if (shouldPublish(subproject)) {
                        configureLibraryPublishing(subproject)
                        subproject.afterEvaluate {
                            configurePublications(subproject)
                        }
                    }
                }
            }
        } else {
            // 在子项目直接应用时
            configureLibraryPublishing(target)
            target.afterEvaluate {
                configurePublications(target)
            }
        }
    }

    private fun shouldPublish(project: Project): Boolean {
        val path = project.path
        // 判断是否属于 core 或 feature 模块
        return path.startsWith(":core:") || path.startsWith(":feature:")
    }

    private fun configureLibraryPublishing(project: Project) {
        project.pluginManager.apply("maven-publish")

        project.extensions.configure<LibraryExtension> {
            publishing {
                singleVariant("release") {
                    withSourcesJar()
                }
            }
        }
    }

    private fun configurePublications(project: Project) {
        project.extensions.configure<PublishingExtension> {
            publications {
                create<MavenPublication>("release") {
                    from(project.components["release"])

                    groupId = "top.chengdongqing.weui"
                    // 将路径转换为 artifactId，如 :core:ui:components -> core-ui-components
                    artifactId = project.path
                        .removePrefix(":")
                        .replace(":", "-")
                    version = "2026.01.01"

                    pom {
                        name.set(project.name)
                        description.set("WeUI Android SDK - ${project.path}")
                    }
                }
            }
            repositories {
                mavenLocal()
            }
        }
    }
}
