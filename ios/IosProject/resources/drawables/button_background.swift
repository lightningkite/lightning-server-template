import XmlToXibRuntime
public extension R.drawable {
    static func button_background() -> CALayer {
        LayerMaker.state(.init(
                normal: LayerMaker.rectGradient(
                    startColor: R.color.colorPrimary, 
                    midColor: nil, 
                    endColor: R.color.colorPrimaryDark, 
                    gradientAngle: -45.0, 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: R.dimen.corner_radius, 
                    topRightRadius: R.dimen.corner_radius, 
                    bottomLeftRadius: R.dimen.corner_radius, 
                    bottomRightRadius: R.dimen.corner_radius
                )
                ,
                selected: nil,
                highlighted: LayerMaker.rectGradient(
                    startColor: R.color.colorPrimary, 
                    midColor: nil, 
                    endColor: R.color.colorPrimaryDark, 
                    gradientAngle: -45.0, 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: R.dimen.corner_radius, 
                    topRightRadius: R.dimen.corner_radius, 
                    bottomLeftRadius: R.dimen.corner_radius, 
                    bottomRightRadius: R.dimen.corner_radius
                )
                ,
                disabled: LayerMaker.rectGradient(
                    startColor: R.color.colorPrimary, 
                    midColor: nil, 
                    endColor: R.color.colorPrimaryDark, 
                    gradientAngle: -45.0, 
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
