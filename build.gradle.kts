// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion: String by extra
    val khrysalisVersion: String by extra
    val androidRuntimeVersion: String by extra
    val rxPlusVersion: String by extra
    repositories {
        mavenLocal()
//        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
//        maven(url = "https://s01.oss.sonatype.org/content/repositories/releases/")
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.lightningkite.khrysalis:plugin:$khrysalisVersion")
        classpath("com.lightningkite.androidlayouttranslator:plugin:$androidRuntimeVersion")
        classpath("com.lightningkite.rx:view-generator-plugin:$rxPlusVersion")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        group = "com.campchef"
        mavenLocal()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
//        maven(url = "https://s01.oss.sonatype.org/content/repositories/releases/")
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}

tasks.create("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}