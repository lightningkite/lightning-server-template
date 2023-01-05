//
// LandingBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import UIKit

public class LandingBinding: XibView {

    @IBOutlet weak private var _background: UIImageView!
    @IBOutlet weak private var _anonLogin: UIButton!
    @IBOutlet weak private var _signUp: UIButton!
    @IBOutlet weak private var _logIn: UIButton!
    public var background: UIImageView { return _background }
    public var anonLogin: UIButton { return _anonLogin }
    public var signUp: UIButton { return _signUp }
    public var logIn: UIButton { return _logIn }
    
    public override func selectNibName() -> String {
       
        return "LandingBinding"
    }

}