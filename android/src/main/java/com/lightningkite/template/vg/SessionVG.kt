@file:SharedCode
//
// LibraryVG.swift
// Created by RxKotlin-Plus Prototype Generator
// Sections of this file will be replaced if the marker, '(overwritten on flow generation)', is left in place.
//
package com.lightningkite.template.vg

//--- Imports

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.khrysalis.Unowned
import com.lightningkite.rx.ValueSubject
import com.lightningkite.rx.android.*
import com.lightningkite.rx.android.resources.*
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.template.databinding.SessionBinding
import com.lightningkite.template.models.Session
import com.lightningkite.template.models.UserSession
import io.reactivex.rxjava3.kotlin.subscribeBy

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class SessionVG(
    //--- Dependencies (overwritten on flow generation)
    @Unowned val root: ViewGeneratorStack,
    val session: Session,
    @Unowned val stack: ViewGeneratorStack,
    //--- Extends
) : ViewGenerator {

    //--- Properties
    val showBackButton = ValueSubject(true)
    val showSettingsButton = ValueSubject(true)

    //--- Provides sessionStack (overwritten on flow generation)
    val sessionStack: ViewGeneratorStack = ValueSubject(listOf())
    
    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = SessionBinding.inflate(dependency.layoutInflater)
        
        //--- Set Up xml.backButton (overwritten on flow generation)
        xml.backButton.onClick { this.backButtonClick() }
        
        //--- Set Up xml.logo

        //--- Set Up xml.title
        sessionStack.map {
            (it.lastOrNull() as? HasTitle)?.title ?: ViewStringRaw("¯\\_(ツ)_/¯")
        }.into(xml.title, TextView::setText)

        sessionStack.into(xml.backButton) { isEnabled = it.size > 1 }
        sessionStack.into(xml.settings) { isEnabled = it.lastOrNull() !is SettingsVG }

        //--- Set Up xml.settings
        showBackButton.into(xml.backButton, ImageButton::visible)
        showBackButton.into(xml.backButton, ImageButton::setClickable)
        showSettingsButton.into(xml.settings, ImageButton::visible)
        showSettingsButton.into(xml.settings, ImageButton::setClickable)

        sessionStack.subscribeBy(
            onNext = {
                showBackButton.value = it.size > 1
                showSettingsButton.value = it.lastOrNull() !is SettingsVG
            },
            onError = { println("An error occurred") }
        )
        xml.settings.onClick {
            settingsClick()
            showSettingsButton.value = false
        }

        //--- Set Up xml.session (overwritten on flow generation)
        sessionStack.showIn(xml.session, dependency)
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    //--- Init
    init {
        //--- Set Initial View for sessionStack (overwritten on flow generation)
        this.sessionStack.reset(SettingsVG(this.root, session))
        
        //--- Init End
    }

    //--- Actions

    //--- Action backButtonClick
    fun backButtonClick() {
        sessionStack.backPressPop()
    }

    //--- Action settingsClick
    fun settingsClick() {
        sessionStack.push(SettingsVG(root = this.root, session = this.session))
    }
    
    
    //--- Body End
}
