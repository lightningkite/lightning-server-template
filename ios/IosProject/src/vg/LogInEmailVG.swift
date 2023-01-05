// Package: com.lightningkite.template.vg
// Generated by Khrysalis - this file will be overwritten.
import KhrysalisRuntime
import RxSwift
import UIKit
import LightningServer
import RxSwiftPlus
import Foundation
import XmlToXibRuntime

public final class LogInEmailVG : ViewGenerator, HasBackAction {
    public unowned var root: ValueSubject<Array<ViewGenerator>>
    public var server: ServerOption
    public init(root: ValueSubject<Array<ViewGenerator>>, server: ServerOption) {
        self.root = root
        self.server = server
        self.email = ValueSubject("")
        self.pinEmail = ValueSubject("-")
        self.pin = ValueSubject("")
        self.working = ValueSubject(false)
        self.emailRegex = (try! NSRegularExpression(pattern: """
            [a-zA-Z0-9._+-]+@[a-z]+\\.+[a-z]+
        """, options: []))
        //Necessary properties should be initialized now
    }
    
    
    public let email: ValueSubject<String>
    public let pinEmail: ValueSubject<String>
    public let pin: ValueSubject<String>
    public let working: ValueSubject<Bool>
    
    private let emailRegex: NSRegularExpression
    
    public func generate(dependency: ViewControllerAccess) -> UIView {
        
        let xml = LogInEmailBinding()
        
        //--- Set Up xml.email
        self.email.bind(xml.email)
        
        //--- Set Up xml.pin
        Observable.combineLatest(self.email, self.pinEmail) { (a, b) -> Bool in a == b }.subscribeAutoDispose(xml.pin, (\UIView.exists))
        self.pin.bind(xml.pin)
        
        //--- Set Up xml.submitWorking
        self.working.subscribeAutoDispose(xml.submitWorking, \UIView.showLoading)
        
        //--- Set Up xml.submitEmail
        Observable.merge(xml.submitEmail.rx.click, Observable<Void>.never().map { (it) -> Void in () }, Observable<Void>.never().map { (it) -> Void in () }).flatMap({ (it) -> Single<Void> in run { () -> Single<Void> in
            if self.email.value == self.pinEmail.value, !self.pin.value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                return self.server.api.auth.emailPINLogin(input: EmailPinLogin(email: self.pinEmail.value, pin: self.pin.value)).flatMap { (it) -> Single<Void> in RootVG.Companion.INSTANCE.instance.login(server: self.server, token: it) }.map { (it) -> Void in () }.working(self.working).doOnError { (it) -> Void in
                    it.printStackTrace()
                    showDialog(message: R.string.generic_error)
                    self.pinEmail.value = "-"
                }.catchError { _ in Single.just(()) }.doOnSuccess { (it) -> Void in xml.pin.becomeFirstResponder() }
            } else {
                return run { () -> Single<Void> in
                    if self.email.value.matches(self.emailRegex) {
                        return self.server.api.auth.emailLoginLink(input: self.email.value)
                            .working(self.working).doOnSuccess { (it) -> Void in
                            self.pinEmail.value = self.email.value
                            showDialog(message: R.string.email_sent)
                        }.doOnError { (it) -> Void in
                            it.printStackTrace()
                            showDialog(message: R.string.generic_error)
                        }.catchError { _ in Single.just(()) }
                    } else {
                        self.email.value = ""
                        showDialog(message: R.string.invalid_email_address)
                        return Single.just(())
                    }
                }
                
            }
        } }).subscribeAutoDispose(xml.submitEmail, { (this, it) -> Void in  })
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root
    }
    
    
    
    //--- Actions
    
    //--- Action submitEmailClick
    
    
    //--- Body End
}

