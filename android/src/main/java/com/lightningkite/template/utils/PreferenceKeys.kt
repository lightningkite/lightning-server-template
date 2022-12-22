@file:SharedCode

package com.lightningkite.template.utils

import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.template.actual.Preferences


object PreferenceKeys {
    const val serverKey: String = "server"
    const val sessionKey = "session"
    const val picturesOnKey = "pictures_on"
    var picturesOn: Boolean
        get() = Preferences.get(picturesOnKey) ?: true
        set(value) { Preferences.set(picturesOnKey, value) }
    const val touchWordsOnKey = "touch_words_on"
    var touchWordsOn: Boolean
        get() = Preferences.get(touchWordsOnKey) ?: true
        set(value) { Preferences.set(touchWordsOnKey, value) }
    const val readToMeOnKey = "read_to_me_on"
    var readToMeOn: Boolean
        get() = Preferences.get(readToMeOnKey) ?: true
        set(value) { Preferences.set(readToMeOnKey, value) }
    const val recordReadingOnKey = "record_reading_on"
    var recordReadingOn: Boolean
        get() = Preferences.get(recordReadingOnKey) ?: true
        set(value) { Preferences.set(recordReadingOnKey, value) }
    const val textSizeKey = "text_size"
    var textSize: Int
        get() = Preferences.get(textSizeKey) ?: 3
        set(value) { Preferences.set(textSizeKey, value) }
}