@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// 开启类型安全项目访问器
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "WeUI"

include(":app")
include(":core:ui:theme")
include(":core:ui:components")
include(":core:data:model")
include(":core:data:repository")
include(":core:utils")

include(":feature:basic")
include(":feature:form")
include(":feature:feedback")
include(":feature:media")
include(":feature:network")
include(":feature:charts")
include(":feature:hardware")
include(":feature:location")
include(":feature:qrcode")
include(":feature:system")
include(":feature:samples")
include(":feature:samples:file-browser")
include(":feature:samples:paint")
include(":feature:samples:video-channel")