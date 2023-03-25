@file:UseContextualSerialization(Instant::class, UUID::class, ServerFile::class)

package com.lightningkite.template

import com.lightningkite.lightningdb.*
import com.lightningkite.lightningserver.cache.LocalCache
import com.lightningkite.lightningserver.ktor.runServer
import com.lightningkite.lightningserver.pubsub.LocalPubSub
import com.lightningkite.lightningserver.settings.loadSettings
import kotlinx.serialization.UseContextualSerialization
import java.io.File
import java.time.Instant
import java.util.*


fun main(){
    Server
    loadSettings(File("settings.json"))
    runServer(LocalPubSub, LocalCache)
}