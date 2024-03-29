// Package: com.lightningkite.template.vg
// Generated by Khrysalis - this file will be overwritten.
import { HomeBinding } from '../../resources/layouts/HomeBinding'
import { ViewGenerator } from '@lightningkite/rxjs-plus'

//! Declares com.lightningkite.template.vg.HomeVG
export class HomeVG implements ViewGenerator {
    public static implementsViewGenerator = true;
    public constructor() {
    }
    
    
    //--- Properties
    
    public generate(dependency: Window): HTMLElement {
        
        const xml = HomeBinding.inflate();
        
        //--- Generate End (overwritten on flow generation)
        
        return xml.root;
    }
    
    
    
    //--- Actions
    
    
    //--- Body End
}
