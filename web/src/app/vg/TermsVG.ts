// Package: com.lightningkite.template.vg
// Generated by Khrysalis - this file will be overwritten.
import { TermsBinding } from '../../resources/layouts/TermsBinding'
import { ViewGenerator, bind, not, onThrottledEventDo, reverse, subscribeAutoDispose } from '@lightningkite/rxjs-plus'
import { BehaviorSubject } from 'rxjs'

//! Declares com.lightningkite.template.vg.TermsVG
export class TermsVG implements ViewGenerator {
    public static implementsViewGenerator = true;
    public constructor(public readonly onResult: (() => void)) {
        this.agreed = new BehaviorSubject(false);
    }
    
    
    public readonly agreed: BehaviorSubject<boolean>;
    
    public generate(dependency: Window): HTMLElement {
        
        const xml = TermsBinding.inflate();
        
        //--- Set Up xml.agree
        this.agreed.pipe(bind(xml.agree.input, "checked", "input"));
        
        //--- Set Up xml.submit
        this.agreed.pipe(subscribeAutoDispose(xml.submit, reverse("disabled", not)));
        onThrottledEventDo(xml.submit, 'click', (): void => {
            if (this.agreed.value) {
                this.onResult();
            }
        });
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root;
    }
    
    
    
    //--- Actions
    
    //--- Action submitClick
    
    
    //--- Body End
}
