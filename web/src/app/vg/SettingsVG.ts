// Package: com.lightningkite.template.vg
// Generated by Khrysalis - this file will be overwritten.
import { Strings } from '../../resources/R'
import { SettingsBinding } from '../../resources/layouts/SettingsBinding'
import { User } from '../../shared/models'
import { LiveApi } from '../api/LiveApi'
import { Session } from '../models/UserSession'
import { HasTitle } from './HasTitle'
import { RootVG } from './RootVG'
import { printStackTrace, tryCastClass } from '@lightningkite/khrysalis-runtime'
import { ViewGenerator, ViewGeneratorStack, hasClass, onThrottledEventDo, setOnWhileActive, subscribeAutoDispose, viewExists } from '@lightningkite/rxjs-plus'
import { BehaviorSubject, Observable, of } from 'rxjs'
import { catchError, map, shareReplay, tap } from 'rxjs/operators'
import { vsprintf } from 'sprintf-js'

//! Declares com.lightningkite.template.vg.SettingsVG
export class SettingsVG implements ViewGenerator, HasTitle {
    public static implementsViewGenerator = true;
    public static implementsHasTitle = true;
    public constructor(public readonly root: ViewGeneratorStack, public readonly session: Session) {
        this.working = new BehaviorSubject(false);
    }
    
    
    public readonly working: BehaviorSubject<boolean>;
    
    //! Declares com.lightningkite.template.vg.SettingsVG.title
    public get title(): string { return Strings.settings; }
    
    
    public generate(dependency: Window): HTMLElement {
        
        const xml = SettingsBinding.inflate();
        
        //--- Set Up xml.welcomeEmail
        const user: Observable<(User | null)> = ((): Observable<(User | null)> => {
            if (this.session.user !== null) {
                return this.session.user!.auth.getSelf().pipe(setOnWhileActive(this.working)).pipe(map((it: User): (User | null) => (it))).pipe(tap(undefined, (it: any): void => {
                    printStackTrace(it);
                })).pipe(catchError(() => of(null))).pipe(shareReplay(1));
            } else {
                return of(null);
            }
        })();
        
        user.pipe(map((it: (User | null)): string => (((): (string | null) => {
            const temp15 = (it?.email ?? null);
            if (temp15 === null || temp15 === undefined) { return null }
            return ((it: string): string => (vsprintf(Strings.user_email, [it])))(temp15)
        })() ?? Strings.anon_account))).pipe(subscribeAutoDispose(xml.welcomeEmail, "innerText"));
        
        //--- Set Up xml.loadWorking
        this.working.pipe(subscribeAutoDispose(xml.loadWorking, hasClass("loading")));
        
        //--- Set Up xml.subscriptionLink
        user.pipe(map((it: (User | null)): boolean => ((it !== null) && (it?.subscriptionId ?? null) === null))).pipe(subscribeAutoDispose(xml.subscriptionLink, viewExists));
        onThrottledEventDo(xml.subscriptionLink, 'click', (): void => {
            window.open(`${((tryCastClass<LiveApi>(this.session.api, LiveApi))?.httpUrl ?? null) ?? "invalid"}/payment?jwt=${this.session.userToken}`, "_blank");
        });
        
        //--- Set Up xml.manageSubscription
        user.pipe(map((it: (User | null)): boolean => ((it?.subscriptionId ?? null) !== null))).pipe(subscribeAutoDispose(xml.manageSubscription, viewExists));
        onThrottledEventDo(xml.manageSubscription, 'click', (): void => {
            window.open(`${((tryCastClass<LiveApi>(this.session.api, LiveApi))?.httpUrl ?? null) ?? "invalid"}/payment/portal?jwt=${this.session.userToken}`, "_blank");
        });
        
        //--- Set Up xml.logout
        onThrottledEventDo(xml.logout, 'click', (): void => {
            this.logoutClick();
        });
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root;
    }
    
    
    
    //--- Actions
    
    public logoutClick(): void {
        RootVG.Companion.INSTANCE.instance.logOut();
    }
    
    
    //--- Body End
}
