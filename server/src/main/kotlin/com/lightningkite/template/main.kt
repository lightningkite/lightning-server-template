@file:UseContextualSerialization(Instant::class, UUID::class, ServerFile::class)

package com.lightningkite.template

import com.lightningkite.kotlinercli.cli
import com.lightningkite.lightningdb.*
import com.lightningkite.lightningserver.aws.terraformAws
import com.lightningkite.lightningserver.cache.LocalCache
import com.lightningkite.lightningserver.ktor.runServer
import com.lightningkite.lightningserver.pubsub.LocalPubSub
import com.lightningkite.lightningserver.settings.loadSettings
import com.lightningkite.lightningserver.typed.Documentable
import com.lightningkite.lightningserver.typed.kotlinSdkLocal
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.UseContextualSerialization
import java.io.File
import java.time.Instant
import java.util.*

fun <Model> allowEverybody() = Condition.Always<Model>()
fun <Model> allowNobody() = Condition.Never<Model>()
fun <Model> allowUsers(user: User?) = if (user != null) Condition.Always<Model>() else Condition.Never()
fun <Model> ModelPermissions<Model>.permissionsIfNotAdmin(user: User?) =
    if (user?.isSuperUser == true) ModelPermissions.allowAll() else this

fun setup() {
    Server
}

private fun serve() {
    loadSettings(File("settings.json"))
    runServer(LocalPubSub, LocalCache)
}

fun createSuperAdmin(email: String) = runBlocking {
    loadSettings(File("settings.json"))
    Server.users.collection().insertOne(User(email = email, isSuperUser = true))
}

fun generateSdk() {
    Documentable.kotlinSdkLocal(
        "com.lightningkite.template.api", File("android/src/main/java/com/lightningkite/template/api").absoluteFile
    )
}

fun terraform() {
    Server
    terraformAws("com.lightningkite.template.AwsHandler", "LKTemplate", File("server/terraform"))
}

fun main(vararg args: String) {
    cli(
        arguments = args,
        setup = ::setup,
        available = listOf(::serve, ::createSuperAdmin, ::generateSdk, ::terraform),
    )
}