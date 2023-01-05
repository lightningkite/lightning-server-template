//
// LogInBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import UIKit

public class LogInBinding: XibView {

    @IBOutlet weak private var _email: UIButton!
    @IBOutlet weak private var _google: UIButton!
    @IBOutlet weak private var _apple: UIButton!
    @IBOutlet weak private var _github: UIButton!
    @IBOutlet weak private var _selectedServer: UILabel!
    public var email: UIButton { return _email }
    public var google: UIButton { return _google }
    public var apple: UIButton { return _apple }
    public var github: UIButton { return _github }
    public var selectedServer: UILabel { return _selectedServer }
    
    public override func selectNibName() -> String {
       
        return "LogInBinding"
    }

}