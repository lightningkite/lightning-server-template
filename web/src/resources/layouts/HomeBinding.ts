import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './home.html'


//! Declares com.lightningkite.template.databinding.HomeBinding
export interface HomeBinding {
    root: HTMLElement
    
    
}

export namespace HomeBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): HomeBinding {
       return inflateHtmlFile(variants, [], {}, {}) as HomeBinding
   }
}
