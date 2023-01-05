//
// LogInEmailBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import UIKit

public class LogInEmailBinding: XibView {

    @IBOutlet weak private var _email: UITextField!
    @IBOutlet weak private var _pin: UITextField!
    @IBOutlet weak private var _submitWorking: UIView!
    @IBOutlet weak private var _submitEmail: UIButton!
    public var email: UITextField { return _email }
    public var pin: UITextField { return _pin }
    public var submitWorking: UIView { return _submitWorking }
    public var submitEmail: UIButton { return _submitEmail }
    
    public override func selectNibName() -> String {
       
        return "LogInEmailBinding"
    }

}