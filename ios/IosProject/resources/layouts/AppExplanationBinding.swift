//
// AppExplanationBinding.swift
// Created by Android Xml to iOS Xib Translator
//

import XmlToXibRuntime
import UIKit

public class AppExplanationBinding: XibView {

    @IBOutlet weak private var _explanation: UICollectionView!
    public var explanation: UICollectionView { return _explanation }
    
    public override func selectNibName() -> String {
       
        return "AppExplanationBinding"
    }

}