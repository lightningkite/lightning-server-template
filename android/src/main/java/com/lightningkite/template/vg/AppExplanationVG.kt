@file:SharedCode
//
// AppExplanationVG.swift
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
class AppExplanationVG(
    //--- Dependencies (overwritten on flow generation)
    //--- Extends
) : ViewGenerator {

    //--- Properties
    
    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = AppExplanationBinding.inflate(dependency.layoutInflater)
        
        //--- Set Up xml.explanation (overwritten on flow generation)
        Observable.just(listOf(1, 2, 3, 4))
            .showIn(xml.explanation) label@ { obs ->
        
            //--- Make Subview For xml.explanation (overwritten on flow generation)
            val cellXml = ComponentAppExplanationBinding.inflate(dependency.layoutInflater)
            
            //--- Set Up cellXml.image (overwritten on flow generation)
            cellXml.image.setImageResource(R.drawable.logo)
            
            //--- Set Up cellXml.title (overwritten on flow generation)
            cellXml.title.setText("Example Text")
            
            //--- Set Up cellXml.content (overwritten on flow generation)
            cellXml.content.setText("Here is a text explanation")
            
            //--- Set Up cellXml.button (overwritten on flow generation)
            cellXml.button.setText("Let\'s go!")
            cellXml.button.onClick { this.cellXmlButtonClick() }
            
            //--- End Make Subview For xml.explanation (overwritten on flow generation)
            return@label cellXml.root
        }
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    //--- Init
    init {
    
    //--- Init End
    }
    
    //--- Actions
    
    //--- Action cellXmlButtonClick (overwritten on flow generation)
    fun cellXmlButtonClick() {
    }
    
    
    //--- Body End
}
