@file:SharedCode
//
// LoginVG.swift
// Created by RxKotlin-Plus Prototype Generator
// Sections of this file will be replaced if the marker, '(overwritten on flow generation)', is left in place.
//
package com.lightningkite.template.vg

//--- Imports

import android.view.View
import android.widget.TextView
import android.widget.ViewFlipper
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.editorActionEvents
import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.khrysalis.Unowned
import com.lightningkite.lightningserver.auth.EmailPinLogin
import com.lightningkite.rx.ValueSubject
import com.lightningkite.rx.android.*
import com.lightningkite.rx.android.resources.ViewStringResource
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.rx.working
import com.lightningkite.template.R
import com.lightningkite.template.api.ServerOption
import com.lightningkite.template.api.ServerOptions
import com.lightningkite.template.databinding.LoginBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class LoginVG(
    //--- Dependencies (overwritten on flow generation)
    val onResult: (server: ServerOption, token: String?) -> Single<Unit>,
    @Unowned val root: ViewGeneratorStack,
    //--- Extends
) : ViewGenerator {

    //--- Properties
    val selectedServer = ValueSubject(0)
    val email = ValueSubject("")
    val pinEmail = ValueSubject("-")
    val pin = ValueSubject("")
    val working = ValueSubject(false)

    private val emailRegex = """[a-zA-Z0-9._+-]+@[a-z]+\.+[a-z]+""".toRegex()

    //--- Generate Start
    override fun generate(dependency: ActivityAccess): View {

        val xml = LoginBinding.inflate(dependency.layoutInflater)

        //--- Set Up xml.email
        email.bind(xml.email)

        //--- Set Up xml.pin
        Observable.combineLatest(email, pinEmail) { a, b -> a == b }.into(xml.pin, View::exists)
        pin.bind(xml.pin)

        //--- Set Up xml.submitWorking
        working.into(xml.submitWorking, ViewFlipper::showLoading)

        //--- Set Up xml.submitEmail
        Observable.merge(
            xml.submitEmail.clicks(),
            xml.pin.editorActionEvents().map { Unit },
            xml.email.editorActionEvents().map { Unit },
        ).flatMapSingle {
            if (email.value == pinEmail.value && pin.value.isNotBlank()) {
                ServerOptions.availableServers[selectedServer.value].api.auth.emailPINLogin(
                    EmailPinLogin(
                        pinEmail.value, pin.value
                    )
                ).flatMap {
                    onResult(
                        ServerOptions.availableServers[selectedServer.value], it
                    )
                }.working(working).doOnError {
                    it.printStackTrace()
                    showDialog(ViewStringResource(R.string.generic_error))
                }.onErrorReturnItem(Unit)
            } else {
                if (email.value.matches(emailRegex)) {
                    ServerOptions.availableServers[selectedServer.value].api.auth.emailLoginLink(email.value)
                        .working(working).doOnSuccess {
                            pinEmail.value = email.value
                            showDialog(ViewStringResource(R.string.email_sent))
                        }.doOnError { showDialog(ViewStringResource(R.string.generic_error)) }.onErrorReturnItem(Unit)
                } else {
                    email.value = ""
                    showDialog(ViewStringResource(R.string.invalid_email_address))
                    Single.just(Unit)
                }

            }
        }.into(xml.submitEmail) {}

        xml.anonLogin.onClick { onResult(ServerOptions.availableServers[selectedServer.value], null) }

        //--- Set Up xml.google
        xml.google.onClick { this.googleClick(dependency) }

        //--- Set Up xml.apple
        xml.apple.onClick { this.appleClick(dependency) }

        //--- Set Up xml.selectedServer
        selectedServer.map { ServerOptions.availableServers[it].name }.into(xml.selectedServer, TextView::setText)
        xml.selectedServer.onLongClick {
            var temp = selectedServer.value + 1
            if (temp >= ServerOptions.availableServers.size) temp = 0
            selectedServer.value = temp
        }

        //--- Generate End (overwritten on flow generation)

        return xml.root
    }

    //--- Init
    init {

        //--- Init End
    }

    //--- Actions

    //--- Action submitEmailClick

    //--- Action googleClick
    fun googleClick(dependency: ActivityAccess) {
        dependency.openUrl(ServerOptions.availableServers[selectedServer.value].api.httpUrl + "/auth/oauth/google/login")
    }

    //--- Action appleClick
    fun appleClick(dependency: ActivityAccess) {
        dependency.openUrl(ServerOptions.availableServers[selectedServer.value].api.httpUrl + "/auth/oauth/apple/login")
    }

    //--- Action submitClick


    //--- Body End
}
