import com.lightningkite.khrysalis.gradle.*

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.lightningkite.khrysalis")
    `maven-publish`
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

sourceSets.main.configure {
    this.java
}

val lightningServerVersion: String by project
val kotlinVersion: String by project
val khrysalisVersion: String by project
dependencies {

    kcp("com.lightningkite.khrysalis:kotlin-compiler-plugin-swift:$khrysalisVersion")
    kcp("com.lightningkite.khrysalis:kotlin-compiler-plugin-typescript:$khrysalisVersion")
    api("com.lightningkite.khrysalis:jvm-runtime:$khrysalisVersion")
    equivalents("com.lightningkite.khrysalis:jvm-runtime:$khrysalisVersion:equivalents")

    ksp("com.lightningkite.lightningserver:processor:$lightningServerVersion")

    api("com.lightningkite.lightningserver:shared:$lightningServerVersion")

    equivalents("com.lightningkite.lightningserver:shared:$lightningServerVersion:equivalents")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

ksp {
    this.blockOtherCompilerPlugins = true
}

khrysalis {
    iosProjectFolder = rootDir.resolve("ios")
    iosSourceFolder = rootDir.resolve("ios/IosProject/shared")

    webProjectFolder = rootDir.resolve("web")
    webSourceFolder = rootDir.resolve("web/src/shared")
}
