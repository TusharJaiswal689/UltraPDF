// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // Version catalog plugin access
    // (No AGP or Kotlin plugin here — they go inside the app module)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
