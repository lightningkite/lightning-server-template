// Package: com.lightningkite.template
// Generated by Khrysalis - this file will be overwritten.
import { Instant } from '@js-joda/core'
import { ReifiedType, setUpDataClass } from '@lightningkite/khrysalis-runtime'
import { HasEmail, HasId } from '@lightningkite/lightning-server'
import { v4 as randomUuidV4 } from 'uuid'

//! Declares com.lightningkite.template.User
export class User implements HasId<string>, HasEmail {
    public static implementsHasId = true;
    public static implementsHasEmail = true;
    public constructor(public readonly _id: string = randomUuidV4(), public readonly email: string, public readonly termsAgreed: Instant = Instant.EPOCH, public readonly isSuperUser: boolean = false, public readonly subscriptionId: (string | null) = null, public readonly customerId: (string | null) = null) {
    }
    public static properties = ["_id", "email", "termsAgreed", "isSuperUser", "subscriptionId", "customerId"]
    public static propertyTypes() { return {_id: [String], email: [String], termsAgreed: [Instant], isSuperUser: [Boolean], subscriptionId: [String], customerId: [String]} }
    copy: (values: Partial<User>) => this;
    equals: (other: any) => boolean;
    hashCode: () => number;
}
setUpDataClass(User)

//! Declares com.lightningkite.template.FcmToken
export class FcmToken implements HasId<string> {
    public static implementsHasId = true;
    public constructor(public readonly _id: string, public readonly user: string) {
    }
    public static properties = ["_id", "user"]
    public static propertyTypes() { return {_id: [String], user: [String]} }
    copy: (values: Partial<FcmToken>) => this;
    equals: (other: any) => boolean;
    hashCode: () => number;
}
setUpDataClass(FcmToken)
