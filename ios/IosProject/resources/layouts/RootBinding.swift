//
// RootBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import RxSwiftPlus
import UIKit

public class RootBinding: XibView {

    @IBOutlet weak private var _content: SwapView!
    @IBOutlet weak private var _backButton: UIButton!
    @IBOutlet weak private var _dialog: SwapView!
    public var content: SwapView { return _content }
    public var backButton: UIButton { return _backButton }
    public var dialog: SwapView { return _dialog }
    
    public override func selectNibName() -> String {
       
        return "RootBinding"
    }

}