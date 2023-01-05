import XmlToXibRuntime
public extension R.drawable {
    static func transparent_button_background() -> CALayer {
        LayerMaker.state(.init(
                normal: LayerMaker.rect(
                    fillColor: R.color.transparentBack, 
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
                    fillColor: R.color.transparentBack, 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: R.dimen.corner_radius, 
                    topRightRadius: R.dimen.corner_radius, 
                    bottomLeftRadius: R.dimen.corner_radius, 
                    bottomRightRadius: R.dimen.corner_radius
                )
                ,
                disabled: LayerMaker.rect(
                    fillColor: R.color.transparentBack, 
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
