// Package: com.lightningkite.template.vg
// Generated by Khrysalis - this file will be overwritten.
import { Strings } from '../../resources/R'
import { LogInEmailBinding } from '../../resources/layouts/LogInEmailBinding'
import { ServerOption } from '../api/ServerOptions'
import { RootVG } from './RootVG'
import { printStackTrace, xCharSequenceIsBlank } from '@lightningkite/khrysalis-runtime'
import { EmailPinLogin } from '@lightningkite/lightning-server'
import { HasBackAction, ViewGenerator, ViewGeneratorStack, bind, hasClass, setOnWhileActive, showDialog, subscribeAutoDispose, viewExists } from '@lightningkite/rxjs-plus'
import { BehaviorSubject, Observable, combineLatest, fromEvent, merge, of } from 'rxjs'
import { catchError, filter, map, mergeMap, tap } from 'rxjs/operators'

//! Declares com.lightningkite.template.vg.LogInEmailVG
export class LogInEmailVG implements ViewGenerator, HasBackAction {
    public static implementsViewGenerator = true;
    public static implementsHasBackAction = true;
    public constructor(public readonly root: ViewGeneratorStack, public readonly server: ServerOption) {
        this.email = new BehaviorSubject("");
        this.pinEmail = new BehaviorSubject("-");
        this.pin = new BehaviorSubject("");
        this.working = new BehaviorSubject(false);
        this.emailRegex = new RegExp("[a-zA-Z0-9._+-]+@[a-z]+\\.+[a-z]+");
    }
    
    
    public readonly email: BehaviorSubject<string>;
    public readonly pinEmail: BehaviorSubject<string>;
    public readonly pin: BehaviorSubject<string>;
    public readonly working: BehaviorSubject<boolean>;
    
    private readonly emailRegex: RegExp;
    
    public generate(dependency: Window): HTMLElement {
        
        const xml = LogInEmailBinding.inflate();
        
        //--- Set Up xml.email
        this.email.pipe(bind(xml.email, "value", "input"));
        
        //--- Set Up xml.pin
        combineLatest([this.email, this.pinEmail], (a: string, b: string): boolean => (a === b)).pipe(subscribeAutoDispose(xml.pin, viewExists));
        this.pin.pipe(bind(xml.pin, "value", "input"));
        
        //--- Set Up xml.submitWorking
        this.working.pipe(subscribeAutoDispose(xml.submitWorking, hasClass("loading")));
        
        //--- Set Up xml.submitEmail
        merge(fromEvent(xml.submitEmail, "click", ev => ev.preventDefault()), fromEvent(xml.pin, "keyup").pipe(filter(x => (x as KeyboardEvent).key === "Enter")).pipe(map((it: KeyboardEvent): void => {
                        return undefined;
            })), fromEvent(xml.email, "keyup").pipe(filter(x => (x as KeyboardEvent).key === "Enter")).pipe(map((it: KeyboardEvent): void => {
                return undefined;
        }))).pipe(mergeMap((it: void): Observable<void> => (((): Observable<void> => {
            if (this.email.value === this.pinEmail.value && !xCharSequenceIsBlank(this.pin.value)) {
                return this.server.api.auth.emailPINLogin(new EmailPinLogin(this.pinEmail.value, this.pin.value)).pipe(mergeMap((it: string): Observable<void> => (RootVG.Companion.INSTANCE.instance.login(this.server, it)))).pipe(map((it: void): void => {
                    return undefined;
                })).pipe(setOnWhileActive(this.working)).pipe(tap(undefined, (it: any): void => {
                    printStackTrace(it);
                    showDialog(Strings.generic_error);
                    this.pinEmail.next("-");
                })).pipe(catchError(() => of(undefined))).pipe(tap((it: void): void => {
                    xml.pin.focus();
                }));
            } else {
                return ((): Observable<void> => {
                    if (this.emailRegex.test(this.email.value)) {
                        return this.server.api.auth.emailLoginLink(this.email.value)
                            .pipe(setOnWhileActive(this.working)).pipe(tap((it: void): void => {
                            this.pinEmail.next(this.email.value);
                            showDialog(Strings.email_sent);
                        })).pipe(tap(undefined, (it: any): void => {
                            printStackTrace(it);
                            showDialog(Strings.generic_error);
                        })).pipe(catchError(() => of(undefined)));
                    } else {
                        this.email.next("");
                        showDialog(Strings.invalid_email_address);
                        return of(undefined);
                    }
                })()
                
            }
        })()))).pipe(subscribeAutoDispose(xml.submitEmail, (this_: HTMLButtonElement, it: void): void => {}));
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root;
    }
    
    public onBackPressed(): boolean {
        if (this.email.value === this.pinEmail.value) {
            this.pinEmail.next("-");
            return true;
        }
        return false;
    }
    
    
    
    //--- Actions
    
    //--- Action submitEmailClick
    
    
    //--- Body End
}
