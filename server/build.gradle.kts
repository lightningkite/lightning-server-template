plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    application
}

group = "com.campchef"

application {
    this.mainClass.set("com.lightingkite.template.MainKt")
}

val lightningServerVersion: String by extra
val exposedVersion: String by extra
dependencies {
    api(project(":shared"))
    api("com.lightningkite.lightningserver:server-core:$lightningServerVersion")
    api("com.lightningkite.lightningserver:server-aws:$lightningServerVersion")
    api("com.lightningkite.lightningserver:server-mongo:$lightningServerVersion")
    api("com.lightningkite.lightningserver:server-firebase:$lightningServerVersion")
    api("com.lightningkite.lightningserver:server-ktor:$lightningServerVersion")
    api("com.lightningkite.lightningserver:server-sentry:$lightningServerVersion")
    ksp("com.lightningkite.lightningserver:processor:$lightningServerVersion")
    implementation("com.stripe:stripe-java:22.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    api("io.sentry:sentry:1.7.30")
    api("io.sentry:sentry-logback:1.7.30")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

tasks.create("buildZip", Zip::class.java) {
    archiveFileName.set("lambda.zip")
    destinationDirectory.set(project.buildDir.resolve("dist"))
    val jarTask = tasks.getByName("jar")
    dependsOn(jarTask)
    val output = jarTask.outputs.files.find { it.extension == "jar" }!!
    println("Output One: ${output}")
    from(zipTree(output))
    into("lib") {
        from(configurations.runtimeClasspath) {
            var index = 0
            rename { s -> (index++).toString() + s }
        }
    }
}
