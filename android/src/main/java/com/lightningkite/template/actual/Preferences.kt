package com.lightningkite.template.actual

import android.content.SharedPreferences
//import com.lightningkite.khrysalis.IsCodable
import com.lightningkite.rx.okhttp.fromJsonString
import com.lightningkite.rx.okhttp.toJsonString

object Preferences {

    lateinit var sharedPreferences: SharedPreferences

    inline fun <reified T> set(key: String, value: T) {
        sharedPreferences.edit().putString(key, value.toJsonString()).apply()
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    inline fun <reified T> get(key: String): T? {
        return sharedPreferences.getString(key, null)?.fromJsonString()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
