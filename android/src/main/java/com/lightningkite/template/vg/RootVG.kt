@file:SharedCode
//
// RootVG.swift
// Created by RxKotlin-Plus Prototype Generator
// Sections of this file will be replaced if the marker, '(overwritten on flow generation)', is left in place.
//
package com.lightningkite.template.vg

//--- Imports

import android.view.View
import android.widget.ImageButton
import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.lightningdb.assign
import com.lightningkite.lightningdb.modification
import com.lightningkite.rx.ValueSubject
import com.lightningkite.rx.android.into
import com.lightningkite.rx.android.onClick
import com.lightningkite.rx.android.resources.ViewStringResource
import com.lightningkite.rx.android.visible
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.template.R
import com.lightningkite.template.actual.SecurePreferences
import com.lightningkite.template.api.ServerOption
import com.lightningkite.template.api.ServerOptions
import com.lightningkite.template.databinding.RootBinding
import com.lightningkite.template.models.AnonymousSession
import com.lightningkite.template.models.Session
import com.lightningkite.template.models.UserSession
import com.lightningkite.template.termsAgreed
import com.lightningkite.template.utils.PreferenceKeys
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.time.Instant

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
        println("Handling deep link!")
        println("$schema://$host$path?$params")

        val option = ServerOptions.availableServers.find {
            it.api.httpUrl.contains(host)
        }

        println("OPTION: $option")
        if (option == null) {
            showDialog(ViewStringResource(R.string.deep_link_was_invalid_server))
        } else {
            params["jwt"]?.let { jwt ->
                login(option, jwt)
                    .doOnSubscribe { dialog.reset(LoadingDialogVG()) }
                    .doOnTerminate { dialog.value = listOf() }
                    .subscribeBy(
                        onError = { showDialog(ViewStringResource(R.string.deep_link_was_invalid_credentials)) },
                        onSuccess = { },
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
                )
            )
            Single.just(Unit)
        } else {
            val session = UserSession(server.api, token)
            session.api.auth.getSelf(session.userToken).doOnSuccess { user ->
                SecurePreferences.set(PreferenceKeys.serverKey, server.name)
                SecurePreferences.set(PreferenceKeys.sessionKey, token)
                if(user.termsAgreed > Instant.EPOCH) {
                    dialog.value = listOf()
                    root.reset(
                        SessionVG(
                            session = Session(
                                AnonymousSession(api = server.api), session
                            ),
                            root = mainStack,
                        )
                    )
                } else {
                    root.push(TermsVG {
                        session.api.user.modify(user._id, modification { it.termsAgreed assign Instant.now() }, token)
                            .subscribeBy(onError = { it.printStackTrace() }, onSuccess = { println(it) })
                        dialog.value = listOf()
                        root.reset(
                            SessionVG(
                                session = Session(
                                    AnonymousSession(api = server.api), session
                                ),
                                root = mainStack,
                            )
                        )
                    })
                }
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

        //--- Set Up xml.backButton
        val showBackButton = root.map { it.size > 1 }
        showBackButton.into(xml.backButton, ImageButton::visible)
        showBackButton.into(xml.backButton, ImageButton::setClickable)
        xml.backButton.onClick { this.backButtonClick() }

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
            login(option, jwt)
                .doOnSubscribe { dialog.reset(LoadingDialogVG()) }
                .doOnTerminate { dialog.value = listOf() }
                .subscribeBy(
                    onError = {
                        SecurePreferences.clear()
                        loginAction()
                    },
                    onSuccess = {
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

    //--- Action backButtonClick (overwritten on flow generation)
    fun backButtonClick() {
        this.root.pop()
    }


    //--- Action sessionAction

    //--- Action loginAction
    fun loginAction() {
        this.root.reset(LandingVG(root, root))
    }

    //--- Body End
}
