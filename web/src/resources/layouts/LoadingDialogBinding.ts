import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './loading_dialog.html'


//! Declares com.lightningkite.template.databinding.LoadingDialogBinding
export interface LoadingDialogBinding {
    root: HTMLElement
    
    
}

export namespace LoadingDialogBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): LoadingDialogBinding {
       return inflateHtmlFile(variants, [], {}, {}) as LoadingDialogBinding
   }
}
