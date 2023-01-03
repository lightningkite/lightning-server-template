@file:SharedCode

package com.lightningkite.template.api

import com.lightningkite.khrysalis.SharedCode

class ServerOption(
    val name: String,
    val api: LiveApi,
)

object ServerOptions {
    val jivie = ServerOption("Jivie", LiveApi(httpUrl = "https://jivie.lightningkite.com", socketUrl = "wss://jivie.lightningkite.com"))

    val availableServers = listOf(jivie)

    fun getOptionByName(name: String) = availableServers.find { it.name == name }

    fun getIndexByName(name: String): Int? {
        val index = availableServers.indexOfFirst { it.name == name }
        return if (index >= 0) index else null
    }
}