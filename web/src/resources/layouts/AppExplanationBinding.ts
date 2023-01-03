import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './app_explanation.html'


//! Declares com.lightningkite.template.databinding.AppExplanationBinding
export interface AppExplanationBinding {
    root: HTMLElement
    explanation: HTMLDivElement & {container: HTMLDivElement, previous: HTMLButtonElement, next: HTMLButtonElement}
    
}

export namespace AppExplanationBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): AppExplanationBinding {
       return inflateHtmlFile(variants, [], {explanation: ["container", "previous", "next"]}, {}) as AppExplanationBinding
   }
}
