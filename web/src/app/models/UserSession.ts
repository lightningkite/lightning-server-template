// Package: com.lightningkite.template.models
// Generated by Khrysalis - this file will be overwritten.
import { Api } from '../api/Api'
import { AbstractAnonymousSession, AbstractUserSession } from '../api/Sessions'
import { ReifiedType, setUpDataClass } from '@lightningkite/khrysalis-runtime'

//! Declares com.lightningkite.template.models.UserSession
export class UserSession extends AbstractUserSession {
    public constructor(public readonly api: Api, public readonly userToken: string) {
        super(api, userToken);
    }
    
}

//! Declares com.lightningkite.template.models.Session
export class Session {
    public constructor(public readonly anon: AnonymousSession, public readonly user: (UserSession | null) = null) {
    }
    public static properties = ["anon", "user"]
    public static propertyTypes() { return {anon: [AnonymousSession], user: [UserSession]} }
    copy: (values: Partial<Session>) => this;
    equals: (other: any) => boolean;
    hashCode: () => number;
    
    //! Declares com.lightningkite.template.models.Session.api
    public get api(): Api { return this.anon.api; }
    
    //! Declares com.lightningkite.template.models.Session.userToken
    public get userToken(): (string | null) { return (this.user?.userToken ?? null); }
    
}
setUpDataClass(Session)

//! Declares com.lightningkite.template.models.AnonymousSession
export class AnonymousSession extends AbstractAnonymousSession {
    public constructor(api: Api) {
        super(api);
    }
    
}