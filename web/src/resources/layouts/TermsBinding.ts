import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './terms.html'


//! Declares com.lightningkite.template.databinding.TermsBinding
export interface TermsBinding {
    root: HTMLElement
    submit: HTMLButtonElement
    agree: HTMLLabelElement & {input: HTMLInputElement, label: HTMLSpanElement}
    
}

export namespace TermsBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): TermsBinding {
       return inflateHtmlFile(variants, ["submit"], {agree: ["input", "label"]}, {}) as TermsBinding
   }
}
