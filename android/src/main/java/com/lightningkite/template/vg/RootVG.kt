@file:SharedCode
//
// RootVG.swift
// Created by RxKotlin-Plus Prototype Generator
// Sections of this file will be replaced if the marker, '(overwritten on flow generation)', is left in place.
//
package com.lightningkite.template.vg

//--- Imports

import android.view.View
import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.rx.ValueSubject
import com.lightningkite.rx.android.resources.ViewStringResource
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.template.R
import com.lightningkite.template.actual.SecurePreferences
import com.lightningkite.template.api.ServerOption
import com.lightningkite.template.api.ServerOptions
import com.lightningkite.template.databinding.RootBinding
import com.lightningkite.template.models.AnonymousSession
import com.lightningkite.template.models.Session
import com.lightningkite.template.models.UserSession
import com.lightningkite.template.utils.PreferenceKeys
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class RootVG(
    //--- Dependencies (overwritten on flow generation)
    //--- Extends
) : ViewGenerator, EntryPoint {
    companion object {
        lateinit var instance: RootVG
    }

    init {
        instance = this
    }

    //--- Properties
    override fun handleDeepLink(schema: String, host: String, path: String, params: Map<String, String>) {

        val option = ServerOptions.availableServers.find {
            it.api.httpUrl.contains(schema) && it.api.httpUrl.contains(host)
        }

        if (option == null) {
            showDialog(ViewStringResource(R.string.deep_link_was_invalid_server))
        } else {
            params["jwt"]?.let { jwt ->
                option.api.auth.getSelf(jwt).subscribeBy(
                    onError = { showDialog(ViewStringResource(R.string.deep_link_was_invalid_credentials)) },
                    onSuccess = {
                        SecurePreferences.set(PreferenceKeys.serverKey, option.name)
                        SecurePreferences.set(PreferenceKeys.sessionKey, jwt)
                        dialog.value = listOf()
                        root.reset(
                            SessionVG(
                                session = Session(
                                    AnonymousSession(api = option.api), UserSession(
                                        api = option.api, userToken = jwt
                                    )
                                ),
                                root = mainStack,
                                stack = mainStack,
                            )
                        )
                    },
                )
            }
        }

    }

    fun login(server: ServerOption, token: String?): Single<Unit> {
        return if (token == null) {
            root.reset(
                SessionVG(
                    session = Session(AnonymousSession(server.api), null),
                    root = mainStack,
                    stack = mainStack,
                )
            )
            Single.just(Unit)
        } else {
            val session = UserSession(server.api, token)
            session.api.auth.getSelf(session.userToken).doOnSuccess { user ->
                SecurePreferences.set(PreferenceKeys.serverKey, server.name)
                SecurePreferences.set(PreferenceKeys.sessionKey, token)
                dialog.value = listOf()
                root.reset(
                    SessionVG(
                        session = Session(
                            AnonymousSession(api = server.api), session
                        ),
                        root = mainStack,
                        stack = mainStack,
                    )
                )
            }.map { Unit }
        }
    }

    //--- Provides dialog (overwritten on flow generation)
    val dialog: ViewGeneratorStack = ValueSubject(listOf())

    //--- Provides root (overwritten on flow generation)
    val root: ViewGeneratorStack = ValueSubject(listOf())

    //--- Back
    override val mainStack: ViewGeneratorStack get() = root

    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {

        val xml = RootBinding.inflate(dependency.layoutInflater)

        //--- Set Up xml.content (overwritten on flow generation)
        root.showIn(xml.content, dependency)

        //--- Set Up xml.dialog (overwritten on flow generation)
        dialog.showIn(xml.dialog, dependency)

        //--- Generate End (overwritten on flow generation)

        return xml.root
    }

    //--- Init
    init {

        //--- Set Initial View for root
        val serverName = SecurePreferences.get<String>(PreferenceKeys.serverKey)
        val option = serverName?.let { ServerOptions.getOptionByName(it) }
        val jwt = SecurePreferences.get<String>(PreferenceKeys.sessionKey)
        if (option != null && jwt != null) {
            option.api.auth.getSelf(jwt).subscribeBy(
                onError = {
                    SecurePreferences.clear()
                    loginAction()
                },
                onSuccess = {
                    dialog.value = listOf()
                    root.reset(
                        SessionVG(
                            session = Session(
                                AnonymousSession(api = option.api), UserSession(
                                    api = option.api, userToken = jwt
                                )
                            ),
                            root = root,
                            stack = root,
                        )
                    )
                },
            )
        } else {
            SecurePreferences.clear()
            loginAction()
        }
//        this.root.reset(ViewGenerator.Default())
//        val session = UserSession(ServerOptions.officeDev.api, "token")
//        session.getBookData(3, 1).subscribeBy {
//            this.root.reset(BookVG(it, session))
//        }

        //--- Init End
    }

    //--- Actions

    //--- Action sessionAction
    fun sessionAction(session: Session) {
        root.push(SessionVG(stack = root, root = this.root, session = session))
    }

    //--- Action loginAction
    fun loginAction() {
        this.root.reset(LoginVG(root = root, onResult = { option, token ->
            login(option, token)
        }))
    }

    //--- Body End
}
