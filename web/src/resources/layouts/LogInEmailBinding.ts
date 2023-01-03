import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './log_in_email.html'


//! Declares com.lightningkite.template.databinding.LogInEmailBinding
export interface LogInEmailBinding {
    root: HTMLElement
    email: HTMLInputElement
    pin: HTMLInputElement
    submitEmail: HTMLButtonElement
    submitWorking: HTMLDivElement
    
}

export namespace LogInEmailBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): LogInEmailBinding {
       return inflateHtmlFile(variants, ["email", "pin", "submitEmail", "submitWorking"], {}, {}) as LogInEmailBinding
   }
}
