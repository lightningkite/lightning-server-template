@file:SharedCode
//
// ProfileVG.swift
// Created by RxKotlin-Plus Prototype Generator
// Sections of this file will be replaced if the marker, '(overwritten on flow generation)', is left in place.
//
package com.lightningkite.template.vg

//--- Imports

import android.widget.*
import android.view.*
import com.lightningkite.rx.*
import com.lightningkite.rx.android.*
import com.lightningkite.rx.android.resources.*
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.khrysalis.*
import com.lightningkite.template.R
import com.lightningkite.template.User
import com.lightningkite.template.api.LiveApi
import com.lightningkite.template.databinding.*
import com.lightningkite.template.databinding.SettingsBinding
import com.lightningkite.template.models.Session
import io.reactivex.rxjava3.core.Single
import java.util.*

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class SettingsVG(
    //--- Dependencies (overwritten on flow generation)
    @Unowned val root: ViewGeneratorStack,
    val session: Session,
    //--- Extends
) : ViewGenerator, HasTitle {

    //--- Properties
    val working = ValueSubject(false)

    override val title: ViewString
        get() = ViewStringResource(R.string.settings)

    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = SettingsBinding.inflate(dependency.layoutInflater)
        
        //--- Set Up xml.welcomeEmail
        val user: Single<Optional<User>> = if (session.user != null) {
            session.user.auth.getSelf().working(working).map { it.optional }.doOnError { it.printStackTrace() }.onErrorReturnItem(Optional.empty()).cache()
        } else {
            Single.just(Optional.empty())
        }

        user.map {
            it.kotlin?.email?.let {
                ViewStringTemplate(
                    ViewStringResource(R.string.user_email),
                    listOf(it)
                )
            } ?: ViewStringResource(R.string.anon_account)
        }.into(xml.welcomeEmail, TextView::setText)

        //--- Set Up xml.loadWorking
        working.into(xml.loadWorking, ViewFlipper::showLoading)

        //--- Set Up xml.subscriptionLink
        user.toObservable().map { it.isPresent && it.kotlin?.subscriptionId == null }.into(xml.subscriptionLink, View::exists)
        xml.subscriptionLink.onClick {
            dependency.openUrl("${(session.api as? LiveApi)?.httpUrl ?: "invalid"}/payment?jwt=${session.userToken}")
        }
        
        //--- Set Up xml.manageSubscription
        user.toObservable().map { it.kotlin?.subscriptionId != null }.into(xml.manageSubscription, View::exists)
        xml.manageSubscription.onClick {
            dependency.openUrl("${(session.api as? LiveApi)?.httpUrl ?: "invalid"}/payment/portal?jwt=${session.userToken}")
        }

        //--- Set Up xml.logout
        xml.logout.onClick { logoutClick() }
        
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
        RootVG.instance.logOut()
    }


    //--- Body End
}
