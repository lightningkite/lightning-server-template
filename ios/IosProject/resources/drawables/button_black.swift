import XmlToXibRuntime
public extension R.drawable {
    static func button_black() -> CALayer {
        LayerMaker.state(.init(
                normal: LayerMaker.rect(
                    fillColor: UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 1.0), 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: R.dimen.corner_radius, 
                    topRightRadius: R.dimen.corner_radius, 
                    bottomLeftRadius: R.dimen.corner_radius, 
                    bottomRightRadius: R.dimen.corner_radius
                )
                ,
                selected: nil,
                highlighted: LayerMaker.rect(
                    fillColor: UIColor(red: 0.06666667, green: 0.06666667, blue: 0.06666667, alpha: 1.0), 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: R.dimen.corner_radius, 
                    topRightRadius: R.dimen.corner_radius, 
                    bottomLeftRadius: R.dimen.corner_radius, 
                    bottomRightRadius: R.dimen.corner_radius
                )
                ,
                disabled: LayerMaker.rect(
                    fillColor: UIColor(red: 0.53333336, green: 0.53333336, blue: 0.53333336, alpha: 1.0), 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: R.dimen.corner_radius, 
                    topRightRadius: R.dimen.corner_radius, 
                    bottomLeftRadius: R.dimen.corner_radius, 
                    bottomRightRadius: R.dimen.corner_radius
                )
                ,
        focused: nil))
    }
}
