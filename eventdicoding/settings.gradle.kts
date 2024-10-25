pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT) // Izinkan repositori di build.gradle
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "eventdicoding"
include(":app")
