@file:SharedCode

package com.lightningkite.template.models

import com.lightningkite.template.api.AbstractUserSession
import com.lightningkite.template.api.Api
import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.rx.okhttp.HttpClient
import com.lightningkite.rx.okhttp.readJson
import com.lightningkite.template.api.AbstractAnonymousSession
import com.lightningkite.template.api.LiveApi
import io.reactivex.rxjava3.core.Single

class UserSession(
    override val api: Api,
    override val userToken: String,
) : AbstractUserSession(api, userToken) {
}

class Session(
    val anon: AnonymousSession,
    val user: UserSession? = null,
) {
    val api: Api get() = anon.api
    val userToken: String? get() = user?.userToken
}

class AnonymousSession(api: Api): AbstractAnonymousSession(api) {
}