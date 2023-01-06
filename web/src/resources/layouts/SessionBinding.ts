import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './session.html'


//! Declares com.lightningkite.template.databinding.SessionBinding
export interface SessionBinding {
    root: HTMLElement
    title: HTMLDivElement
    titleBar: HTMLDivElement
    session: HTMLDivElement
    backButton: HTMLButtonElement & {image: HTMLImageElement}
    homeTab: HTMLLabelElement & {input: HTMLInputElement, label: HTMLSpanElement}
    settingsTab: HTMLLabelElement & {input: HTMLInputElement, label: HTMLSpanElement}
    
}

export namespace SessionBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): SessionBinding {
       return inflateHtmlFile(variants, ["title", "titleBar", "session"], {backButton: ["image"], homeTab: ["input", "label"], settingsTab: ["input", "label"]}, {}) as SessionBinding
   }
}
