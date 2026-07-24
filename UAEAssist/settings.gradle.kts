pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "UAE Assist"
include(":app")
include(":core-navigation")
include(":core-common")
//include(":feature:auth")

include(":feature-auth")
include(":core-network")
include(":feature-profile")
include(":feature-home")
include(":core-socket")
include(":feature-location")
include(":feature-chat_with_us")
include(":core-location")
