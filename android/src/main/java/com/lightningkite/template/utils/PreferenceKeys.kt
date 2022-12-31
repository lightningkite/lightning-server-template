@file:SharedCode

package com.lightningkite.template.utils

import com.lightningkite.khrysalis.SharedCode


object PreferenceKeys {
    const val serverKey: String = "server"
    const val sessionKey = "session"

    var server: String?
        get() = SecurePreferences.getString(serverKey, null)
        set(value) {
            SecurePreferences.edit().putString(serverKey, value).apply()
        }
    var session: String?
        get() = SecurePreferences.getString(sessionKey, null)
        set(value) {
            SecurePreferences.edit().putString(sessionKey, value).apply()
        }
}