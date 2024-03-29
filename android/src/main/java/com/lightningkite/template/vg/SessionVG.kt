@file:SharedCode
//
// LibraryVG.swift
// Created by RxKotlin-Plus Prototype Generator
// Sections of this file will be replaced if the marker, '(overwritten on flow generation)', is left in place.
//
package com.lightningkite.template.vg

//--- Imports

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.khrysalis.Unowned
import com.lightningkite.rx.ValueSubject
import com.lightningkite.rx.android.*
import com.lightningkite.rx.android.resources.*
import com.lightningkite.rx.kotlin
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.rx.viewgenerators.fcm.Notifications
import com.lightningkite.rx.withWrite
import com.lightningkite.template.FcmToken
import com.lightningkite.template.R
import com.lightningkite.template.databinding.SessionBinding
import com.lightningkite.template.models.Session
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class SessionVG(
    //--- Dependencies (overwritten on flow generation)
    @Unowned val root: ViewGeneratorStack,
    val session: Session,
    //--- Extends
) : ViewGenerator {

    //--- Properties

    //--- Provides sessionStack (overwritten on flow generation)
    val sessionStack: ViewGeneratorStack = ValueSubject(listOf())
    
    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = SessionBinding.inflate(dependency.layoutInflater)
        
        //--- helpers
        val showBackButton = sessionStack.map { it.size > 1 }
        Notifications.configure(dependency)
        session.user?.let { userSession ->
            Notifications.notificationToken.switchMapSingle {
                it.kotlin?.let {
                    userSession.auth.getSelf().flatMap { user ->
                        userSession.fcmToken.upsert(it, FcmToken(_id = it, user = user._id))
                    }.map { Unit }
                } ?: Single.just(Unit)
            }.subscribeBy(
                onError = { it.printStackTrace() }
            )
        }

        //--- Set Up xml.titleBar (overwritten on flow generation)
        
        //--- Set Up xml.backButton (overwritten on flow generation)
        xml.backButton.onClick { this.backButtonClick() }
        
        //--- Set Up xml.logo

        //--- Set Up xml.title
        sessionStack.map {
            (it.lastOrNull() as? HasTitle)?.title ?: ViewStringResource(R.string.app_name)
        }.into(xml.title, TextView::setText)

        sessionStack.into(xml.backButton) { isEnabled = it.size > 1 }

        //--- Set Up xml.settings
        showBackButton.into(xml.backButton, ImageButton::visible)
        showBackButton.into(xml.backButton, ImageButton::setEnabled)

        //--- Set Up xml.session (overwritten on flow generation)
        sessionStack.showIn(xml.session, dependency)
        
        //--- Set Up xml.homeTab
        sessionStack.map { it.first() is HomeVG }
            .withWrite { if(it) homeTabClick() }
            .bindNoUncheck(xml.homeTab)
        
        //--- Set Up xml.settingsTab
        sessionStack.map { it.first() is SettingsVG }
            .withWrite { if(it) settingsTabClick() }
            .bindNoUncheck(xml.settingsTab)
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    //--- Init
    init {
        homeTabClick()
        //--- Init End
    }

    //--- Actions

    //--- Action backButtonClick
    fun backButtonClick() {
        sessionStack.backPressPop()
    }

    //--- Action homeTabClick (overwritten on flow generation)
    fun homeTabClick() {
        this.sessionStack.reset(HomeVG())
    }
    
    //--- Action settingsTabClick (overwritten on flow generation)
    fun settingsTabClick() {
        this.sessionStack.reset(SettingsVG(root = this.root, session = this.session))
    }
    
    
    //--- Body End
}
