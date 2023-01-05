//
// R.swift
// Created by Khrysalis
//

import Foundation
import UIKit
import XmlToXibRuntime


public enum R {
    public enum drawable {
        public static func ic_apple() -> CALayer { return CAImageLayer(UIImage(named: "ic_apple.png")) }
        public static func ic_back() -> CALayer { return CAImageLayer(UIImage(named: "ic_back.png")) }
        public static func ic_back_arrow() -> CALayer { return CAImageLayer(UIImage(named: "ic_back_arrow.png")) }
        public static func ic_circle_notifications() -> CALayer { return CAImageLayer(UIImage(named: "ic_circle_notifications.png")) }
        public static func ic_close() -> CALayer { return CAImageLayer(UIImage(named: "ic_close.png")) }
        public static func ic_email() -> CALayer { return CAImageLayer(UIImage(named: "ic_email.png")) }
        public static func ic_email_login() -> CALayer { return CAImageLayer(UIImage(named: "ic_email_login.png")) }
        public static func ic_filter() -> CALayer { return CAImageLayer(UIImage(named: "ic_filter.png")) }
        public static func ic_forward() -> CALayer { return CAImageLayer(UIImage(named: "ic_forward.png")) }
        public static func ic_github_mark() -> CALayer { return CAImageLayer(UIImage(named: "ic_github_mark.png")) }
        public static func ic_google() -> CALayer { return CAImageLayer(UIImage(named: "ic_google.png")) }
        public static func ic_home() -> CALayer { return CAImageLayer(UIImage(named: "ic_home.png")) }
        public static func ic_launcher_foreground() -> CALayer { return CAImageLayer(UIImage(named: "ic_launcher_foreground.png")) }
        public static func ic_notification() -> CALayer { return CAImageLayer(UIImage(named: "ic_notification.png")) }
        public static func ic_payment() -> CALayer { return CAImageLayer(UIImage(named: "ic_payment.png")) }
        public static func ic_profile() -> CALayer { return CAImageLayer(UIImage(named: "ic_profile.png")) }
        public static func ic_settings() -> CALayer { return CAImageLayer(UIImage(named: "ic_settings.png")) }
        public static func landing_background() -> CALayer { return CAImageLayer(UIImage(named: "landing_background.jpg")) }
        public static func logo() -> CALayer { return CAImageLayer(UIImage(named: "logo.png")) }
        public static func logo_title() -> CALayer { return CAImageLayer(UIImage(named: "logo_title.png")) }
        public static let allEntries: Dictionary<String, ()->CALayer> = [
            "button_accent": button_accent,
            "button_background": button_background,
            "button_black": button_black,
            "button_toggle": button_toggle,
            "button_white": button_white,
            "ic_apple": ic_apple,
            "ic_back": ic_back,
            "ic_back_arrow": ic_back_arrow,
            "ic_circle_notifications": ic_circle_notifications,
            "ic_close": ic_close,
            "ic_email": ic_email,
            "ic_email_login": ic_email_login,
            "ic_filter": ic_filter,
            "ic_forward": ic_forward,
            "ic_github_mark": ic_github_mark,
            "ic_google": ic_google,
            "ic_home": ic_home,
            "ic_launcher_foreground": ic_launcher_foreground,
            "ic_notification": ic_notification,
            "ic_payment": ic_payment,
            "ic_profile": ic_profile,
            "ic_settings": ic_settings,
            "landing_background": landing_background,
            "launch_background": launch_background,
            "logo": logo,
            "logo_title": logo_title,
            "textedit_background": textedit_background,
            "transparent_button_background": transparent_button_background
        ]
    }
    public enum string {
        public static let already_have_account = NSLocalizedString("I already have an account", comment: "already_have_account")
        public static let anon_account = NSLocalizedString("You are not logged in.", comment: "anon_account")
        public static let anon_log_in = NSLocalizedString("Show me what it's like", comment: "anon_log_in")
        public static let app_name = NSLocalizedString("LK Template", comment: "app_name")
        public static let app_subtitle = NSLocalizedString("Strikingly good software at breakneck speeds.", comment: "app_subtitle")
        public static let apple = NSLocalizedString("Apple", comment: "apple")
        public static let back = NSLocalizedString("Back", comment: "back")
        public static let books = NSLocalizedString("Books", comment: "books")
        public static let check_email = NSLocalizedString("A link to log in to the app was sent to your email!", comment: "check_email")
        public static let deep_link_intent = NSLocalizedString("Deep Link", comment: "deep_link_intent")
        public static let deep_link_was_invalid_credentials = NSLocalizedString("The deep link was invalid. Login credentials did not work.", comment: "deep_link_was_invalid_credentials")
        public static let deep_link_was_invalid_server = NSLocalizedString("The deep link was invalid. Could not find a server that matched the link.", comment: "deep_link_was_invalid_server")
        public static let emailAddress = NSLocalizedString("Email Address", comment: "emailAddress")
        public static let email_sent = NSLocalizedString("Email sent! You can either click the link in your email OR enter the received PIN.", comment: "email_sent")
        public static let enter_email_to_login = NSLocalizedString("Enter your email address to log into the app:", comment: "enter_email_to_login")
        public static let generic_error = NSLocalizedString("An error occurred", comment: "generic_error")
        public static let google = NSLocalizedString("Google", comment: "google")
        public static let home = NSLocalizedString("Home", comment: "home")
        public static let i_agree = NSLocalizedString("I agree to the terms presented", comment: "i_agree")
        public static let invalid_email_address = NSLocalizedString("Please enter a valid email address.", comment: "invalid_email_address")
        public static let log_in = NSLocalizedString("Log In", comment: "log_in")
        public static let log_out = NSLocalizedString("Log Out", comment: "log_out")
        public static let my_profile = NSLocalizedString("My Profile", comment: "my_profile")
        public static let or = NSLocalizedString("Or,", comment: "or")
        public static let pin = NSLocalizedString("PIN", comment: "pin")
        public static let settings = NSLocalizedString("Settings", comment: "settings")
        public static let sign_in_apple = NSLocalizedString("Sign in with Apple", comment: "sign_in_apple")
        public static let sign_in_email = NSLocalizedString("Sign in with Email", comment: "sign_in_email")
        public static let sign_in_github = NSLocalizedString("Sign in with GitHub", comment: "sign_in_github")
        public static let sign_in_google = NSLocalizedString("Sign in with Google", comment: "sign_in_google")
        public static let sign_up = NSLocalizedString("Sign Up", comment: "sign_up")
        public static let submit = NSLocalizedString("Submit", comment: "submit")
        public static let subscribe = NSLocalizedString("Subscribe", comment: "subscribe")
        public static let subscription_link = NSLocalizedString("Manage Your Subscription", comment: "subscription_link")
        public static let terms_of_service = NSLocalizedString("Terms of Service", comment: "terms_of_service")
        public static let terms_of_service_body = NSLocalizedString("lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum", comment: "terms_of_service_body")
        public static let try_it_out = NSLocalizedString("Try It Out", comment: "try_it_out")
        public static let user_email = NSLocalizedString("You are logged in as %1$s", comment: "user_email")
        public static let welcome_x = NSLocalizedString("\"Welcome, %1$s!\"", comment: "welcome_x")
    }
    public enum dimen {
        public static let corner_radius: CGFloat = 8.0
        public static let spacing: CGFloat = 8.0
        public static let spacing2: CGFloat = 16.0
    }
    public enum color {
        public static let background: UIColor = UIColor(named: "color_background")!
        public static let backgroundAlpha: UIColor = UIColor(named: "color_backgroundAlpha")!
        public static let barForeground: UIColor = UIColor(named: "color_barForeground")!
        public static let black: UIColor = UIColor(named: "color_black")!
        public static let colorAccent: UIColor = UIColor(named: "color_colorAccent")!
        public static let colorAccentDark: UIColor = UIColor(named: "color_colorAccentDark")!
        public static let colorPrimary: UIColor = UIColor(named: "color_colorPrimary")!
        public static let colorPrimaryDark: UIColor = UIColor(named: "color_colorPrimaryDark")!
        public static let disabled: UIColor = UIColor(named: "color_disabled")!
        public static let foreground: UIColor = UIColor(named: "color_foreground")!
        public static let foregroundFade: UIColor = UIColor(named: "color_foregroundFade")!
        public static let highlight: UIColor = UIColor(named: "color_highlight")!
        public static let ic_launcher_background: UIColor = UIColor(named: "color_ic_launcher_background")!
        public static let lightBlue: UIColor = UIColor(named: "color_lightBlue")!
        public static let toggleOff: UIColor = UIColor(named: "color_toggleOff")!
        public static let toggleOn: UIColor = UIColor(named: "color_toggleOn")!
        public static let transparent: UIColor = UIColor(named: "color_transparent")!
        public static let transparentBack: UIColor = UIColor(named: "color_transparentBack")!
    }
}
