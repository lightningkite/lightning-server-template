//
// ComponentAppExplanationBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import UIKit

public class ComponentAppExplanationBinding: XibView {

    @IBOutlet weak private var _image: UIImageView!
    @IBOutlet weak private var _title: UILabel!
    @IBOutlet weak private var _content: UILabel!
    @IBOutlet weak private var _button: UIButton!
    public var image: UIImageView { return _image }
    public var title: UILabel { return _title }
    public var content: UILabel { return _content }
    public var button: UIButton { return _button }
    
    public override func selectNibName() -> String {
       
        return "ComponentAppExplanationBinding"
    }

}