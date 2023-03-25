plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    application
}

group = "com.lightningkite.template"

application {
    this.mainClass.set("com.lightingkite.template.MainKt")
}

val lightningServerVersion: String by extra
dependencies {
    api("com.lightningkite.lightningserver:server-core:$lightningServerVersion")
    api("com.lightningkite.lightningserver:server-ktor:$lightningServerVersion")
    ksp("com.lightningkite.lightningserver:processor:$lightningServerVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

