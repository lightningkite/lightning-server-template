// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion: String by extra
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    repositories {
        group = "com.lightningkite.template"
        mavenLocal()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
//        maven(url = "https://s01.oss.sonatype.org/content/repositories/releases/")
        google()
        mavenCentral()
    }
}

tasks.create("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}