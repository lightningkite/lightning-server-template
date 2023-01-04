@file:SharedCode
//
// LogInEmailVG.swift
// Created by RxKotlin-Plus Prototype Generator
// Sections of this file will be replaced if the marker, '(overwritten on flow generation)', is left in place.
//
package com.lightningkite.template.vg

//--- Imports

import android.widget.*
import android.view.*
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.editorActionEvents
import com.lightningkite.rx.*
import com.lightningkite.rx.android.*
import com.lightningkite.rx.android.resources.*
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.khrysalis.*
import com.lightningkite.lightningserver.auth.EmailPinLogin
import com.lightningkite.template.R
import com.lightningkite.template.api.ServerOption
import com.lightningkite.template.api.ServerOptions
import com.lightningkite.template.databinding.*
import com.lightningkite.template.models.AnonymousSession
import com.lightningkite.template.models.Session
import com.lightningkite.template.models.UserSession
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class LogInEmailVG(
    //--- Dependencies (overwritten on flow generation)
    @Unowned val root: ViewGeneratorStack,
    val server: ServerOption,
    //--- Extends
) : ViewGenerator, HasBackAction {

    //--- Properties
    val email = ValueSubject("")
    val pinEmail = ValueSubject("-")
    val pin = ValueSubject("")
    val working = ValueSubject(false)

    private val emailRegex = Regex("""[a-zA-Z0-9._+-]+@[a-z]+\.+[a-z]+""")

    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = LogInEmailBinding.inflate(dependency.layoutInflater)
        
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
                server.api.auth.emailPINLogin(
                    EmailPinLogin(
                        pinEmail.value, pin.value
                    )
                ).flatMap {
                    RootVG.instance.login(server, it)
                }.map { Unit }.working(working).doOnError {
                    it.printStackTrace()
                    showDialog(ViewStringResource(R.string.generic_error))
                    pinEmail.value = "-"
                }.onErrorReturnItem(Unit).doOnSuccess {
                    xml.pin.requestFocus()
                }
            } else {
                if (email.value.matches(emailRegex)) {
                    server.api.auth.emailLoginLink(email.value)
                        .working(working).doOnSuccess {
                            pinEmail.value = email.value
                            showDialog(ViewStringResource(R.string.email_sent))
                        }.doOnError {
                            it.printStackTrace()
                            showDialog(ViewStringResource(R.string.generic_error))
                        }.onErrorReturnItem(Unit)
                } else {
                    email.value = ""
                    showDialog(ViewStringResource(R.string.invalid_email_address))
                    Single.just(Unit)
                }

            }
        }.into(xml.submitEmail) {}

        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }

    override fun onBackPressed(): Boolean {
        if(email.value == pinEmail.value) {
            pinEmail.value = "-"
            return true
        }
        return false
    }
    
    //--- Init
    init {

        //--- Init End
    }

    //--- Actions

    //--- Action submitEmailClick


    //--- Body End
}
