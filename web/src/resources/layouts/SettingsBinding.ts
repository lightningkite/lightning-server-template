import {inflateHtmlFile} from "@lightningkite/android-xml-runtime";
import html from './settings.html'


//! Declares com.lightningkite.template.databinding.SettingsBinding
export interface SettingsBinding {
    root: HTMLElement
    welcomeEmail: HTMLDivElement
    subscriptionLink: HTMLButtonElement
    manageSubscription: HTMLButtonElement
    logout: HTMLButtonElement
    loadWorking: HTMLDivElement
    
}

export namespace SettingsBinding {
   const variants = [{
    html: html,
    widerThan: undefined,
    tallerThan: undefined
}]
   export function inflate(): SettingsBinding {
       return inflateHtmlFile(variants, ["welcomeEmail", "subscriptionLink", "manageSubscription", "logout", "loadWorking"], {}, {}) as SettingsBinding
   }
}
