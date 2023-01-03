import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './landing.html'


//! Declares com.lightningkite.template.databinding.LandingBinding
export interface LandingBinding {
    root: HTMLElement
    background: HTMLImageElement
    anonLogin: HTMLButtonElement
    signUp: HTMLButtonElement
    logIn: HTMLButtonElement
    
}

export namespace LandingBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): LandingBinding {
       return inflateHtmlFile(variants, ["background", "anonLogin", "signUp", "logIn"], {}, {}) as LandingBinding
   }
}
