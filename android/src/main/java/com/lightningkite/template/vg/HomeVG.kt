@file:SharedCode
//
// HomeVG.swift
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
class HomeVG(
    //--- Dependencies (overwritten on flow generation)
    //--- Extends
) : ViewGenerator {

    //--- Properties
    
    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = HomeBinding.inflate(dependency.layoutInflater)
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    //--- Init
    init {
    
    //--- Init End
    }
    
    //--- Actions
    
    
    //--- Body End
}
