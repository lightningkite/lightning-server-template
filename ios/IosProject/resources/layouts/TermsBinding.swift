//
// TermsBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import XmlToXibRuntime
import UIKit

public class TermsBinding: XibView {

    @IBOutlet weak private var _agree: LabeledToggle!
    @IBOutlet weak private var _submit: UIButton!
    public var agree: LabeledToggle { return _agree }
    public var submit: UIButton { return _submit }
    
    public override func selectNibName() -> String {
       
        return "TermsBinding"
    }

}