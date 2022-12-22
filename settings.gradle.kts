pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    plugins {
        kotlin("plugin.serialization") version kotlinVersion
        id("com.google.devtools.ksp") version kspVersion
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("com.android.application") version "7.0.4"
        id("org.jetbrains.kotlin.android") version kotlinVersion
    }
}

include(":server")
include(":android")
include(":shared")
