@file:SharedCode
//
// LandingVG.swift
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
import com.lightningkite.template.databinding.*
import io.reactivex.rxjava3.core.Observable

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class LandingVG(
    //--- Dependencies (overwritten on flow generation)
    @Unowned val root: ViewGeneratorStack,
    @Unowned val stack: ViewGeneratorStack,
    //--- Extends
) : ViewGenerator {

    //--- Properties
    
    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = LandingBinding.inflate(dependency.layoutInflater)
        
        //--- Set Up xml.background (overwritten on flow generation)
        
        //--- Set Up xml.anonLogin (overwritten on flow generation)
        xml.anonLogin.onClick { this.anonLoginClick() }
        
        //--- Set Up xml.signUp (overwritten on flow generation)
        xml.signUp.onClick { this.signUpClick() }
        
        //--- Set Up xml.logIn (overwritten on flow generation)
        xml.logIn.onClick { this.logInClick() }
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    //--- Init
    init {
    
    //--- Init End
    }
    
    //--- Actions
    
    //--- Action anonLoginClick (overwritten on flow generation)
    fun anonLoginClick() {
        stack.push(AppExplanationVG())
    }
    
    //--- Action signUpClick (overwritten on flow generation)
    fun signUpClick() {
        stack.push(LogInVG(root = this.root, stack = this.stack))
    }
    
    //--- Action logInClick (overwritten on flow generation)
    fun logInClick() {
        stack.push(LogInVG(root = this.root, stack = this.stack))
    }
    
    
    //--- Body End
}
