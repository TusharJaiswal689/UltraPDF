pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Good practice
    repositories {        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs")
    }
}


rootProject.name = "UltraPDF"

// Include your modules here
include(":app")
