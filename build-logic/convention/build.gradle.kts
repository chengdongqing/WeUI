plugins {
    `kotlin-dsl`
}

group = "top.chengdongqing.weui.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.room.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        val prefix = "weui"
        register("androidComposeApplication") {
            id = "$prefix.android.compose.application"
            implementationClass = "AndroidComposeApplicationConventionPlugin"
        }
        register("androidComposeLibrary") {
            id = "$prefix.android.compose.library"
            implementationClass = "AndroidComposeLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = "$prefix.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidMavenPublish") {
            id = "$prefix.android.maven.publish"
            implementationClass = "AndroidMavenPublishConventionPlugin"
        }
    }
}