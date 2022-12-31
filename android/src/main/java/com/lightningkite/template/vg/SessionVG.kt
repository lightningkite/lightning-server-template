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
import android.widget.ToggleButton
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
        
        //--- Set Up xml.titleBar (overwritten on flow generation)
        
        //--- Set Up xml.backButton (overwritten on flow generation)
        xml.backButton.onClick { this.backButtonClick() }
        
        //--- Set Up xml.logo

        //--- Set Up xml.title
        sessionStack.map {
            (it.lastOrNull() as? HasTitle)?.title ?: ViewStringRaw("¯\\_(ツ)_/¯")
        }.into(xml.title, TextView::setText)

        sessionStack.into(xml.backButton) { isEnabled = it.size > 1 }

        //--- Set Up xml.settings
        showBackButton.into(xml.backButton, ImageButton::visible)
        showBackButton.into(xml.backButton, ImageButton::setClickable)

        //--- Set Up xml.session (overwritten on flow generation)
        sessionStack.showIn(xml.session, dependency)
        
        //--- Set Up xml.mainTab
        sessionStack.map { it.first() is SettingsVG }.into(xml.mainTab, ToggleButton::setChecked)
        xml.mainTab.onClick { this.mainTabClick() }
        
        //--- Set Up xml.altTab
        sessionStack.map { it.first() is SettingsVG }.into(xml.altTab, ToggleButton::setChecked)
        xml.altTab.onClick { this.altTabClick() }
        
        //--- Set Up xml.settingsTab
        sessionStack.map { it.first() is SettingsVG }.into(xml.settingsTab, ToggleButton::setChecked)
        xml.settingsTab.onClick { this.settingsTabClick() }
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    //--- Init
    init {
        mainTabClick()
        //--- Init End
    }

    //--- Actions

    //--- Action backButtonClick
    fun backButtonClick() {
        sessionStack.backPressPop()
    }

    //--- Action mainTabClick (overwritten on flow generation)
    fun mainTabClick() {
        this.sessionStack.reset(SettingsVG(root = this.root, session = this.session))
    }
    
    //--- Action altTabClick (overwritten on flow generation)
    fun altTabClick() {
        this.sessionStack.reset(SettingsVG(root = this.root, session = this.session))
    }
    
    //--- Action settingsTabClick (overwritten on flow generation)
    fun settingsTabClick() {
        this.sessionStack.reset(SettingsVG(root = this.root, session = this.session))
    }
    
    
    //--- Body End
}
