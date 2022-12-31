@file:SharedCode

package com.lightningkite.template.api

import com.lightningkite.khrysalis.SharedCode

class ServerOption(
    val name: String,
    val api: LiveApi,
)

object ServerOptions {
    val local = ServerOption("Local", LiveApi(httpUrl = "http://192.168.1.112:80", socketUrl = "ws://192.168.1.112:80"))

    val availableServers = listOf(local)

    fun getOptionByName(name: String) = availableServers.find { it.name == name }

    fun getIndexByName(name: String): Int? {
        val index = availableServers.indexOfFirst { it.name == name }
        return if (index >= 0) index else null
    }
}