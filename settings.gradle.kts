pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
    }

    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "UltraPDF"

// Include your modules here
include(":app")
