@file: SharedCode
package com.lightningkite.template.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.rx.android.staticApplicationContext


val Preferences by lazy {
    staticApplicationContext.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
}
val SecurePreferences: EncryptedSharedPreferences by lazy {
    EncryptedSharedPreferences.create(
        staticApplicationContext,
        "secret_shared_prefs",
        MasterKey.Builder(staticApplicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    ) as EncryptedSharedPreferences
}