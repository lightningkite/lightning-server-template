// Package: com.lightningkite.template.utils
// Generated by Khrysalis - this file will be overwritten.
import KhrysalisRuntime
import RxSwiftPlus
import Foundation

public var Preferences: UserDefaultsProtocol = { () -> UserDefaultsProtocol in UserDefaults.standard }()
public var SecurePreferences: KeychainUserDefaults = { () -> KeychainUserDefaults in KeychainUserDefaults.shared as! KeychainUserDefaults }()
