import XmlToXibRuntime
public extension R.drawable {
    static func button_white() -> CALayer {
        LayerMaker.state(.init(
                normal: LayerMaker.rect(
                    fillColor: UIColor(red: 1.0, green: 1.0, blue: 1.0, alpha: 1.0), 
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
                    fillColor: UIColor(red: 0.93333334, green: 0.93333334, blue: 0.93333334, alpha: 1.0), 
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
