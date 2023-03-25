pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    plugins {
        kotlin("plugin.serialization") version kotlinVersion
        id("com.google.devtools.ksp") version kspVersion
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
    }
}

include(":server")
