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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "My Application"
include(":app")
include(":app:unitconverter")
include(":app:clockremake")
include(":app:calculator")
include(":app:expensify")
include(":app:note")
include(":app:questionaire")
include(":app:vocabulary")
include(":app:reader")
include(":app:music")
include(":app:cooking")
include(":app:quote")
include(":app:health")
include(":app:weather")
include(":app:texttospeech")
