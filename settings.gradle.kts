pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6+"
}

stonecutter {
    create(rootProject) {
        versions("1.21.1", "1.21.4", "1.21.5", "1.21.7")
        vcsVersion = "1.21.7"
    }
}