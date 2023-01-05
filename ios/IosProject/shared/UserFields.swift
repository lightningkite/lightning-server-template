// Package: com.lightningkite.template
// Generated by Khrysalis - this file will be overwritten.
import KhrysalisRuntime
import LightningServer
import Foundation

public func prepareUserFields() -> Void {
    
    
    
    
    
    
}
public extension PropChain where To == User {
    var _id: PropChain<From, UUID> {
        get { return self.get(prop: User._idProp) }
    }
}
public extension PropChain where To == User {
    var email: PropChain<From, String> {
        get { return self.get(prop: User.emailProp) }
    }
}
public extension PropChain where To == User {
    var termsAgreed: PropChain<From, Date> {
        get { return self.get(prop: User.termsAgreedProp) }
    }
}
public extension PropChain where To == User {
    var isSuperUser: PropChain<From, Bool> {
        get { return self.get(prop: User.isSuperUserProp) }
    }
}
public extension PropChain where To == User {
    var subscriptionId: PropChain<From, String?> {
        get { return self.get(prop: User.subscriptionIdProp) }
    }
}
public extension PropChain where To == User {
    var customerId: PropChain<From, String?> {
        get { return self.get(prop: User.customerIdProp) }
    }
}
