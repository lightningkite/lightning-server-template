@file:SharedCode
//
// ProfileVG.swift
// Created by RxKotlin-Plus Prototype Generator
// Sections of this file will be replaced if the marker, '(overwritten on flow generation)', is left in place.
//
package com.lightningkite.template.vg

//--- Imports

import android.content.ContentResolver
import android.provider.Settings.Global.getString
import android.widget.*
import android.view.*
import com.lightningkite.rx.*
import com.lightningkite.rx.android.*
import com.lightningkite.rx.android.resources.*
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.khrysalis.*
import com.lightningkite.template.R
import com.lightningkite.template.actual.Preferences
import com.lightningkite.template.api.LiveApi
import com.lightningkite.template.databinding.*
import com.lightningkite.template.databinding.SettingsBinding
import com.lightningkite.template.models.Session
import com.lightningkite.template.models.UserSession
import com.lightningkite.template.utils.PreferenceKeys
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class SettingsVG(
    //--- Dependencies (overwritten on flow generation)
    @Unowned val root: ViewGeneratorStack,
    val session: Session,
    //--- Extends
) : ViewGenerator, HasTitle {

    //--- Properties
    val working = ValueSubject(true)

    override val title: ViewString
        get() = ViewStringResource(R.string.settings)

    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {

        val xml = SettingsBinding.inflate(dependency.layoutInflater)

        //--- Set Up xml.loadWorking
        working.showLoading(xml.loadWorking)

        //--- Set Up xml.welcomeEmail
        if (session.user != null) {
            session.user.auth.getSelf().working(working).subscribeBy(
                onSuccess = {
                    xml.welcomeEmail.setText(
                        ViewStringTemplate(
                            ViewStringResource(R.string.user_email),
                            listOf(it.email)
                        )
                    )
                    if (it.subscriptionId != null) {
                        xml.manageSubscription.onClick { dependency.openUrl("${(session.api as? LiveApi)?.httpUrl ?: "invalid"}/subscriptions/portal?jwt=${session.userToken}") }
                        xml.manageSubscription.visibility = View.VISIBLE
                    }
                },
                onError = {
                    println("An error occurred")
                }
            ).addTo(xml.root.removed)
        } else {
            working.value = false
            xml.welcomeEmail.setText(ViewStringResource(R.string.anon_account))
        }

        //--- Set Up xml.logout (overwritten on flow generation)
        xml.logout.onClick { this.logoutClick() }

        //--- Generate End (overwritten on flow generation)

        return xml.root
    }

    //--- Init
    init {

        //--- Init End
    }

    //--- Actions

    //--- Action logoutClick
    fun logoutClick() {
        RootVG.instance.loginAction()
    }


    //--- Body End
}
