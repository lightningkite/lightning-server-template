/*
R.ts
Created by Khrysalis
*/
//! Declares com.lightningkite.template.R
export interface StringInterface {
    already_have_account: string
    anon_account: string
    anon_log_in: string
    app_name: string
    app_subtitle: string
    apple: string
    back: string
    books: string
    check_email: string
    deep_link_intent: string
    deep_link_was_invalid_credentials: string
    deep_link_was_invalid_server: string
    emailAddress: string
    email_sent: string
    enter_email_to_login: string
    generic_error: string
    google: string
    home: string
    i_agree: string
    invalid_email_address: string
    log_in: string
    log_out: string
    my_profile: string
    or: string
    pin: string
    settings: string
    sign_in_apple: string
    sign_in_email: string
    sign_in_github: string
    sign_in_google: string
    sign_up: string
    submit: string
    subscribe: string
    subscription_link: string
    terms_of_service: string
    terms_of_service_body: string
    try_it_out: string
    user_email: string
    welcome_x: string
}
export namespace DefaultStrings {
    export const already_have_account = "I already have an account"
    export const anon_account = "You are not logged in."
    export const anon_log_in = "Show me what it's like"
    export const app_name = "LK Template"
    export const app_subtitle = "Strikingly good software at breakneck speeds."
    export const apple = "Apple"
    export const back = "Back"
    export const books = "Books"
    export const check_email = "A link to log in to the app was sent to your email!"
    export const deep_link_intent = "Deep Link"
    export const deep_link_was_invalid_credentials = "The deep link was invalid. Login credentials did not work."
    export const deep_link_was_invalid_server = "The deep link was invalid. Could not find a server that matched the link."
    export const emailAddress = "Email Address"
    export const email_sent = "Email sent!"
    export const enter_email_to_login = "Enter your email address to log into the app:"
    export const generic_error = "An error occurred"
    export const google = "Google"
    export const home = "Home"
    export const i_agree = "I agree to the terms presented"
    export const invalid_email_address = "Please enter a valid email address."
    export const log_in = "Log In"
    export const log_out = "Log Out"
    export const my_profile = "My Profile"
    export const or = "Or,"
    export const pin = "PIN"
    export const settings = "Settings"
    export const sign_in_apple = "Sign in with Apple"
    export const sign_in_email = "Sign in with Email"
    export const sign_in_github = "Sign in with GitHub"
    export const sign_in_google = "Sign in with Google"
    export const sign_up = "Sign Up"
    export const submit = "Submit"
    export const subscribe = "Subscribe"
    export const subscription_link = "Manage Your Subscription"
    export const terms_of_service = "Terms of Service"
    export const terms_of_service_body = "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
    export const try_it_out = "Try It Out"
    export const user_email = "You are logged in as %1$s"
    export const welcome_x = "\"Welcome, %1$s!\""
}
export const Strings: StringInterface = Object.assign({}, DefaultStrings);
export namespace Colors {
    export const background = "#272727ff"
    export const backgroundAlpha = "#27272780"
    export const barForeground = "#ffffffff"
    export const black = "#000000ff"
    export const colorAccent = "#fcb912ff"
    export const colorAccentDark = "#7e5d0aff"
    export const colorPrimary = "#133e4cff"
    export const colorPrimaryDark = "#0e2f3aff"
    export const disabled = "#888888ff"
    export const foreground = "#ffffffff"
    export const foregroundFade = "#ffffff88"
    export const highlight = "#0000ffff"
    export const ic_launcher_background = "#393939ff"
    export const lightBlue = "#406f9fff"
    export const toggleOff = "#925857ff"
    export const toggleOn = "#58443bff"
    export const transparent = "#00000000"
    export const transparentBack = "#88888888"
}
export namespace Dimen {
    export const corner_radius = "corner_radius"
    export const spacing = "spacing"
    export const spacing2 = "spacing2"
}
import drawable_ic_apple from "./drawables/ic_apple.svg"
import drawable_ic_back from "./drawables/ic_back.svg"
import drawable_ic_back_arrow from "./drawables/ic_back_arrow.svg"
import drawable_ic_close from "./drawables/ic_close.svg"
import drawable_ic_email from "./drawables/ic_email.svg"
import drawable_ic_email_login from "./drawables/ic_email_login.svg"
import drawable_ic_filter from "./drawables/ic_filter.svg"
import drawable_ic_forward from "./drawables/ic_forward.svg"
import drawable_ic_github_mark from "./drawables/ic_github_mark.svg"
import drawable_ic_google from "./drawables/ic_google.svg"
import drawable_ic_home from "./drawables/ic_home.svg"
import drawable_ic_launcher_foreground from "./drawables/ic_launcher_foreground.svg"
import drawable_ic_notification from "./drawables/ic_notification.png"
import drawable_ic_profile from "./drawables/ic_profile.svg"
import drawable_ic_settings from "./drawables/ic_settings.svg"
import drawable_landing_background from "./drawables/landing_background.jpg"
import drawable_logo from "./drawables/logo.svg"
import drawable_logo_title from "./drawables/logo_title.svg"
export namespace Drawables {
    export const button_accent = {name: "button_accent"}
    export const button_background = {name: "button_background"}
    export const button_black = {name: "button_black"}
    export const button_toggle = {name: "button_toggle"}
    export const button_white = {name: "button_white"}
    export const ic_apple = {name: "ic_apple", file: drawable_ic_apple}
    export const ic_back = {name: "ic_back", file: drawable_ic_back}
    export const ic_back_arrow = {name: "ic_back_arrow", file: drawable_ic_back_arrow}
    export const ic_close = {name: "ic_close", file: drawable_ic_close}
    export const ic_email = {name: "ic_email", file: drawable_ic_email}
    export const ic_email_login = {name: "ic_email_login", file: drawable_ic_email_login}
    export const ic_filter = {name: "ic_filter", file: drawable_ic_filter}
    export const ic_forward = {name: "ic_forward", file: drawable_ic_forward}
    export const ic_github_mark = {name: "ic_github_mark", file: drawable_ic_github_mark}
    export const ic_google = {name: "ic_google", file: drawable_ic_google}
    export const ic_home = {name: "ic_home", file: drawable_ic_home}
    export const ic_launcher_foreground = {name: "ic_launcher_foreground", file: drawable_ic_launcher_foreground}
    export const ic_notification = {name: "ic_notification", file: drawable_ic_notification}
    export const ic_profile = {name: "ic_profile", file: drawable_ic_profile}
    export const ic_settings = {name: "ic_settings", file: drawable_ic_settings}
    export const landing_background = {name: "landing_background", file: drawable_landing_background}
    export const launch_background = {name: "launch_background"}
    export const logo = {name: "logo", file: drawable_logo}
    export const logo_title = {name: "logo_title", file: drawable_logo_title}
    export const textedit_background = {name: "textedit_background"}
    export const transparent_button_background = {name: "transparent_button_background"}
}
