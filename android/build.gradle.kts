import com.lightningkite.convertlayout.gradle.androidLayoutConverter
import com.lightningkite.khrysalis.gradle.khrysalis
import com.lightningkite.rx.gradle.PrototyperPluginExtension
import java.io.FileInputStream
import java.util.*

val kotlinVersion: String by project

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.lightningkite.androidlayouttranslator")
    id("com.lightningkite.rx")
    id("com.google.gms.google-services")
    id("com.lightningkite.khrysalis")
    kotlin("plugin.serialization")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.lightningkite.template"
        minSdk = 24
        targetSdk = 33
        versionCode = 5
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources.pickFirsts.add("META-INF/okhttp.kotlin_module")
        resources.pickFirsts.add("META-INF/shared.kotlin_module")
        resources.pickFirsts.add("META-INF/DEPENDENCIES")
    }

    buildFeatures {
        viewBinding = true
    }

//    sourceSets {
//        this.getByName("main").java.srcDir(file("build/generated/ksp/debug/kotlin"))
//    }
}
val lightningServerVersion: String by project
val rxPlusVersion: String by project
val khrysalisVersion: String by project
val androidRuntimeVersion: String by extra
dependencies {

    equivalents("com.lightningkite.lightningserver:client:$lightningServerVersion:equivalents")
    equivalents("com.lightningkite.lightningserver:shared:$lightningServerVersion:equivalents")
    equivalents("com.lightningkite.rx:rxplus:$rxPlusVersion:equivalents")
    equivalents("com.lightningkite.khrysalis:jvm-runtime:$khrysalisVersion:equivalents")
    equivalents("com.lightningkite.androidlayouttranslator:android-runtime:$androidRuntimeVersion:equivalents")

    implementation(project(":shared"))

    implementation("com.lightningkite.lightningserver:client:$lightningServerVersion")
    implementation("com.lightningkite.rx:view-generator:$rxPlusVersion")
    implementation("com.lightningkite.rx:okhttp:$rxPlusVersion")
    implementation("com.lightningkite.rx:okhttp-resources:$rxPlusVersion")
    implementation("com.lightningkite.khrysalis:jvm-runtime:$khrysalisVersion")
    implementation("com.lightningkite.androidlayouttranslator:android-runtime:$androidRuntimeVersion")

    kcp("com.lightningkite.khrysalis:kotlin-compiler-plugin-swift:$khrysalisVersion")
    kcp("com.lightningkite.khrysalis:kotlin-compiler-plugin-typescript:$khrysalisVersion")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

configure<PrototyperPluginExtension> {
    injectKhrysalisAnnotations = true
}

khrysalis {
}

androidLayoutConverter {
}