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
import com.lightningkite.lightningdb.condition
import com.lightningkite.lightningdb.eq
import com.lightningkite.lightningdb.modification
import com.lightningkite.rx.ValueSubject
import com.lightningkite.rx.android.into
import com.lightningkite.rx.android.onClick
import com.lightningkite.rx.android.resources.ViewStringResource
import com.lightningkite.rx.android.visible
import com.lightningkite.rx.kotlin
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.rx.viewgenerators.fcm.ForegroundNotificationHandler
import com.lightningkite.rx.viewgenerators.fcm.ForegroundNotificationHandlerResult
import com.lightningkite.rx.viewgenerators.fcm.Notifications
import com.lightningkite.template.R
import com.lightningkite.template._id
import com.lightningkite.template.api.ServerOption
import com.lightningkite.template.api.ServerOptions
import com.lightningkite.template.databinding.RootBinding
import com.lightningkite.template.models.AnonymousSession
import com.lightningkite.template.models.Session
import com.lightningkite.template.models.UserSession
import com.lightningkite.template.termsAgreed
import com.lightningkite.template.user
import com.lightningkite.template.utils.PreferenceKeys
import com.lightningkite.template.utils.SecurePreferences
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.time.Instant

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class RootVG(
    //--- Dependencies (overwritten on flow generation)
    //--- Extends
) : ViewGenerator, EntryPoint, ForegroundNotificationHandler {
    override fun handleNotificationInForeground(map: Map<String, String>): ForegroundNotificationHandlerResult {
        return ForegroundNotificationHandlerResult.ShowNotification
    }

    override fun onBackPressed(): Boolean {
        return dialog.backPressPop() || mainStack.backPressPop()
    }

    companion object {
        lateinit var instance: RootVG
    }

    init {
        instance = this
    }

    //--- Properties
    override fun handleDeepLink(schema: String, host: String, path: String, params: Map<String, String>) {
        println("Handling deep link!")
        println("$schema://$host$path?${params.entries.joinToString("&") { it.key + "=" + it.value }}")

        val option = ServerOptions.availableServers.find {
            it.api.httpUrl.contains(params["server"] ?: "*NEVER*", true)
        }

        params["jwt"]?.let { jwt ->
            println("OPTION: $option")
            if (option == null) {
                showDialog(ViewStringResource(R.string.deep_link_was_invalid_server))
            } else {
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
                PreferenceKeys.server = server.name
                PreferenceKeys.session = token
                if (user.termsAgreed > Instant.EPOCH) {
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
        showBackButton.into(xml.backButton, ImageButton::setEnabled)
        xml.backButton.onClick { this.backButtonClick() }

        //--- Set Up xml.dialog (overwritten on flow generation)
        dialog.showIn(xml.dialog, dependency)
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    //--- Init
    init {

        //--- Set Initial View for root
        val serverName = PreferenceKeys.server
        val option = serverName?.let { ServerOptions.getOptionByName(it) }
        val jwt = PreferenceKeys.session
        if (option != null && jwt != null) {
            login(option, jwt)
                .doOnSubscribe { dialog.reset(LoadingDialogVG()) }
                .doOnTerminate { dialog.value = listOf() }
                .subscribeBy(
                    onError = {
                        it.printStackTrace()
                        showDialog(ViewStringResource(R.string.generic_error))
                        loginAction()
                    },
                    onSuccess = {
                    },
                )
        } else {
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

    //--- Action backButtonClick
    fun backButtonClick() {
        this.root.backPressPop()
    }
    
    
    //--- Action sessionAction

    //--- Action loginAction
    fun loginAction() {
        this.root.reset(LandingVG(root, root))
    }

    fun logOut(session: Session) {
        session.user?.let {
            Notifications.notificationToken.value.kotlin?.let { token ->
                it.fcmToken.bulkDelete(condition { it._id eq token })
                    .subscribeBy(onError = {}, onSuccess = {})
            }
        }
        SecurePreferences.edit().clear().apply()
        loginAction()
    }

    //--- Body End
}
