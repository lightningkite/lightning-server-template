import XmlToXibRuntime
public extension R.drawable {
    static func button_toggle() -> CALayer {
        LayerMaker.state(.init(
                normal: LayerMaker.rect(
                    fillColor: R.color.colorPrimary, 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: 0, 
                    topRightRadius: 0, 
                    bottomLeftRadius: 0, 
                    bottomRightRadius: 0
                )
                ,
                selected: LayerMaker.rect(
                    fillColor: R.color.colorPrimaryDark, 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: 0, 
                    topRightRadius: 0, 
                    bottomLeftRadius: 0, 
                    bottomRightRadius: 0
                )
                ,
                highlighted: LayerMaker.rect(
                    fillColor: R.color.colorPrimaryDark, 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: 0, 
                    topRightRadius: 0, 
                    bottomLeftRadius: 0, 
                    bottomRightRadius: 0
                )
                ,
                disabled: LayerMaker.rect(
                    fillColor: UIColor(red: 0.53333336, green: 0.53333336, blue: 0.53333336, alpha: 1.0), 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: 0, 
                    topRightRadius: 0, 
                    bottomLeftRadius: 0, 
                    bottomRightRadius: 0
                )
                ,
        focused: nil))
    }
}
