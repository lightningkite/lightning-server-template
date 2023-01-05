//
// SessionBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import UIKit
import RxSwiftPlus
import XmlToXibRuntime

public class SessionBinding: XibView {

    @IBOutlet weak private var _titleBar: UIStackView!
    @IBOutlet weak private var _backButton: UIButton!
    @IBOutlet weak private var _title: UILabel!
    @IBOutlet weak private var _session: SwapView!
    @IBOutlet weak private var _homeTab: ToggleButton!
    @IBOutlet weak private var _settingsTab: ToggleButton!
    public var titleBar: UIStackView { return _titleBar }
    public var backButton: UIButton { return _backButton }
    public var title: UILabel { return _title }
    public var session: SwapView { return _session }
    public var homeTab: ToggleButton { return _homeTab }
    public var settingsTab: ToggleButton { return _settingsTab }
    
    public override func selectNibName() -> String {
       
        return "SessionBinding"
    }

}