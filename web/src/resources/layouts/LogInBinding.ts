import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './log_in.html'


//! Declares com.lightningkite.template.databinding.LogInBinding
export interface LogInBinding {
    root: HTMLElement
    email: HTMLButtonElement
    google: HTMLButtonElement
    apple: HTMLButtonElement
    github: HTMLButtonElement
    selectedServer: HTMLDivElement
    
}

export namespace LogInBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): LogInBinding {
       return inflateHtmlFile(variants, ["email", "google", "apple", "github", "selectedServer"], {}, {}) as LogInBinding
   }
}
