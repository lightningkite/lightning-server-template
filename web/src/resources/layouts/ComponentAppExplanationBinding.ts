import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './component_app_explanation.html'


//! Declares com.lightningkite.template.databinding.ComponentAppExplanationBinding
export interface ComponentAppExplanationBinding {
    root: HTMLElement
    image: HTMLImageElement
    title: HTMLDivElement
    content: HTMLDivElement
    button: HTMLButtonElement
    
}

export namespace ComponentAppExplanationBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): ComponentAppExplanationBinding {
       return inflateHtmlFile(variants, ["image", "title", "content", "button"], {}, {}) as ComponentAppExplanationBinding
   }
}
