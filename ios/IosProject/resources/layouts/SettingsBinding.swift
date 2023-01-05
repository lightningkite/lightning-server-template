//
// SettingsBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import UIKit

public class SettingsBinding: XibView {

    @IBOutlet weak private var _loadWorking: UIView!
    @IBOutlet weak private var _welcomeEmail: UILabel!
    @IBOutlet weak private var _subscriptionLink: UIButton!
    @IBOutlet weak private var _manageSubscription: UIButton!
    @IBOutlet weak private var _logout: UIButton!
    public var loadWorking: UIView { return _loadWorking }
    public var welcomeEmail: UILabel { return _welcomeEmail }
    public var subscriptionLink: UIButton { return _subscriptionLink }
    public var manageSubscription: UIButton { return _manageSubscription }
    public var logout: UIButton { return _logout }
    
    public override func selectNibName() -> String {
       
        return "SettingsBinding"
    }

}