pluginManagement {
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

include(":feature:charts")
include(":feature:hardware")
include(":feature:location")
include(":feature:qrcode")
include(":feature:system")
include(":feature:demos")
include(":feature:demos:gallery")
include(":feature:demos:file-browser")
include(":feature:demos:paint")
include(":feature:demos:video-channel")
include(":feature:demos:image-cropper")
