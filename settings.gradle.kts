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
include(":feature:demos")
include(":feature:demos:file-browser")
include(":feature:demos:paint")
include(":feature:demos:video-channel")