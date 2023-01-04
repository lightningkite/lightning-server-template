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
import com.google.android.gms.common.api.Api.ApiOptions
import com.jakewharton.rxbinding4.view.clicks
import com.lightningkite.rx.*
import com.lightningkite.rx.android.*
import com.lightningkite.rx.android.resources.*
import com.lightningkite.rx.viewgenerators.*
import com.lightningkite.khrysalis.*
import com.lightningkite.template.R
import com.lightningkite.template.api.ServerOptions
import com.lightningkite.template.databinding.*
import com.lightningkite.template.models.AnonymousSession
import com.lightningkite.template.models.Session
import io.reactivex.rxjava3.core.Observable

//--- Name (overwritten on flow generation)
@Suppress("NAME_SHADOWING")
class AppExplanationVG(
    //--- Dependencies (overwritten on flow generation)
    @Unowned val root: ViewGeneratorStack,
    @Unowned val stack: ViewGeneratorStack,
    //--- Extends
) : ViewGenerator {

    //--- Properties
    class Explanation(
        val title: String,
        val image: Image,
        val content: String,
        val buttonTitle: String? = null,
        val button: ()->Unit = {},
    )
    val explanations = listOf(
        Explanation(
            title = "Welcome!",
            image = ImageResource(R.drawable.logo),
            content = "Welcome to Lightning Template!  Some features:",
        ),
        Explanation(
            title = "Notifications",
            image = ImageResource(R.drawable.ic_circle_notifications),
            content = "Notifications on Android, iOS, and Web are all built in.  You just need to plug in your credentials!"
        ),
        Explanation(
            title = "Stripe",
            image = ImageResource(R.drawable.ic_payment),
            content = "Stripe subscriptions are a built-in feature for you to monetize your app!"
        ),
        Explanation(
            title = "Join Us!",
            image = ImageResource(R.drawable.logo),
            content = "We'd love for you to work with our tools!",
            buttonTitle = "Check it out!",
            button = {
                stack.reset(SessionVG(root, Session(AnonymousSession(ServerOptions.availableServers.first().api), null)))
            }
        )
    )
    
    //--- Generate Start (overwritten on flow generation)
    override fun generate(dependency: ActivityAccess): View {
    
        val xml = AppExplanationBinding.inflate(dependency.layoutInflater)
        
        //--- Set Up xml.explanation
        Observable.just(explanations)
            .showIn(xml.explanation) label@ { obs ->
        
            //--- Make Subview For xml.explanation (overwritten on flow generation)
            val cellXml = ComponentAppExplanationBinding.inflate(dependency.layoutInflater)
            
            //--- Set Up cellXml.image
                obs.map {it.image}.into(cellXml.image, ImageView::setImage)

            //--- Set Up cellXml.title
                obs.map {it.title}.into(cellXml.title, TextView::setText)

            //--- Set Up cellXml.content
                obs.map {it.content}.into(cellXml.content, TextView::setText)
            
            //--- Set Up cellXml.button
                obs.map {it.buttonTitle != null }.into(cellXml.button, View::exists)
                obs.map {it.buttonTitle ?: ""}.into(cellXml.button, TextView::setText)
                cellXml.button.clicks().flatMapSingle { obs.firstOrError() }.into(cellXml.root) {
                    it.button()
                }

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
