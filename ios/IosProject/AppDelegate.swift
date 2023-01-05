//
//  AppDelegate.swift
//  IosProject
//
//  Created by Joseph on 1/5/23.
//

import UIKit
import RxSwiftPlus
import Firebase

@UIApplicationMain
class AppDelegate: ViewGeneratorFcmDelegate {
    
    override func makeMain() -> ViewGenerator {
        UIView.backgroundLayersByName = R.drawable.allEntries
        UIView.useLayoutSubviewsLambda()
        
        return RootVG()
    }
    
}



