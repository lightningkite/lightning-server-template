@file:SharedCode
//
// TermsVG.swift
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
class TermsVG(
    //--- Dependencies (overwritten on flow generation)
    val onResult: ()->Unit,
    //--- Extends
) : ViewGenerator {

    //--- Properties
    val agreed = ValueSubject(false)
    
    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = TermsBinding.inflate(dependency.layoutInflater)
        
        //--- Set Up xml.agree
        agreed.bind(xml.agree)
        
        //--- Set Up xml.submit
        agreed.into(xml.submit, Button::setEnabled)
        xml.submit.onClick {
            if(agreed.value) {
                onResult()
            }
        }
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    //--- Init
    init {
    
    //--- Init End
    }
    
    //--- Actions
    
    //--- Action submitClick
    
    
    //--- Body End
}
