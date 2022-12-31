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
import com.lightningkite.template.api.ServerOptions
import com.lightningkite.template.databinding.LogInBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class LogInVG(
    //--- Dependencies (overwritten on flow generation)
    @Unowned val root: ViewGeneratorStack,
    @Unowned val stack: ViewGeneratorStack,
    //--- Extends
) : ViewGenerator {

    //--- Properties
    val selectedServer = ValueSubject(0)

    //--- Generate Start
    override fun generate(dependency: ActivityAccess): View {

        val xml = LogInBinding.inflate(dependency.layoutInflater)

        //--- Set Up xml.email (overwritten on flow generation)
        xml.email.onClick { this.emailClick() }
        
        //--- Set Up xml.google
        xml.google.onClick { this.googleClick(dependency) }

        //--- Set Up xml.apple
        xml.apple.onClick { this.appleClick(dependency) }

        //--- Set Up xml.github
        xml.github.onClick { this.githubClick(dependency) }
        
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

    //--- Action emailClick
    fun emailClick() {
        stack.push(LogInEmailVG(root = this.root, server = ServerOptions.availableServers[selectedServer.value]))
    }
    
    //--- Action submitEmailClick

    //--- Action googleClick
    fun googleClick(dependency: ActivityAccess) {
        dependency.openUrl(ServerOptions.availableServers[selectedServer.value].api.httpUrl + "/auth/oauth/google/login")
    }

    //--- Action appleClick
    fun appleClick(dependency: ActivityAccess) {
        dependency.openUrl(ServerOptions.availableServers[selectedServer.value].api.httpUrl + "/auth/oauth/apple/login")
    }

    //--- Action githubClick
    fun githubClick(dependency: ActivityAccess) {
        dependency.openUrl(ServerOptions.availableServers[selectedServer.value].api.httpUrl + "/auth/oauth/github/login")
    }
    
    
    //--- Action submitClick


    //--- Body End
}
