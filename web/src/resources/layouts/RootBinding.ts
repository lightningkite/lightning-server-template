import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './root.html'


//! Declares com.lightningkite.template.databinding.RootBinding
export interface RootBinding {
    root: HTMLElement
    content: HTMLDivElement
    dialog: HTMLDivElement
    backButton: HTMLButtonElement & {image: HTMLImageElement}
    
}

export namespace RootBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): RootBinding {
       return inflateHtmlFile(variants, ["content", "dialog"], {backButton: ["image"]}, {}) as RootBinding
   }
}
