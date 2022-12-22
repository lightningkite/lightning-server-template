@file:SharedCode

package com.lightningkite.template.api

import com.lightningkite.khrysalis.SharedCode

class ServerOption(
    val name: String,
    val api: LiveApi,
)

object ServerOptions {
    //    val production = ServerOption("    ", LiveApi(httpUrl = "https://bookapi.lazyone.com", socketUrl = "wss://bookapi.lazyone.com"))
//    val local = ServerOption("Local", LiveApi(httpUrl = "http://172.19.17.151:8080", socketUrl = "ws://172.19.17.151:8080"))
//    val officeDev = ServerOption(
//        "Office Dev",
//        LiveApi(httpUrl = "https://bsvedin.lightningkite.com", socketUrl = "wss://bsvedin.lightningkite.com")
//    )
//    val production = ServerOption("    ", LiveApi(httpUrl = "https://bookapi.lazyone.com", socketUrl = "wss://bookapi.lazyone.com"))
//    val local = ServerOption("Local", LiveApi(httpUrl = "http://172.19.17.151:8080", socketUrl = "ws://172.19.17.151:8080"))
    val ivieleague = ServerOption(
        "Ivieleague",
        LiveApi(httpUrl = "https://iseesam.ivieleague.com", socketUrl = "wss://ws.iseesam.ivieleague.com")
    )
    val officeDev = ServerOption(
        "Office Dev",
        LiveApi(httpUrl = "https://jivie.lightningkite.com", socketUrl = "wss://jivie.lightningkite.com")
    )
//    val staging = ServerOption("Staging", LiveApi(httpUrl = "TODO: Fill out here."))

    val availableServers = listOf(ivieleague, officeDev)

    fun getOptionByName(name: String) = availableServers.find { it.name == name }

    fun getIndexByName(name: String): Int? {
        val index = availableServers.indexOfFirst { it.name == name }
        return if (index >= 0) index else null
    }
}