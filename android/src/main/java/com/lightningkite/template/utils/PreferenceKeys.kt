@file:SharedCode

package com.lightningkite.template.utils

import com.lightningkite.khrysalis.SharedCode


object PreferenceKeys {
    const val serverKey: String = "server"
    const val sessionKey = "session"

    var server: String?
        get() = SecurePreferences.getString(serverKey, null)
        set(value) {
            value?.let {
                SecurePreferences.edit().putString(serverKey, it).apply()
            } ?: SecurePreferences.edit().remove(serverKey).apply()
        }
    var session: String?
        get() = SecurePreferences.getString(sessionKey, null)
        set(value) {
            value?.let {
                SecurePreferences.edit().putString(sessionKey, it).apply()
            } ?: SecurePreferences.edit().remove(sessionKey).apply()
        }
}