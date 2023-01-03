// Package: com.lightningkite.template
// Generated by Khrysalis - this file will be overwritten.
import { prepareFcmTokenFields } from './FcmTokenFields'
import { prepareUserFields } from './UserFields'

//! Declares com.lightningkite.template.prepareModels
export function prepareModels(): void {
    prepareUserFields();
    prepareFcmTokenFields();
}
