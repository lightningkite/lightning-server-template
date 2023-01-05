import XmlToXibRuntime
public extension R.drawable {
    static func textedit_background() -> CALayer {
        LayerMaker.rect(
            fillColor: UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.0), 
            strokeColor: R.color.foreground, 
            strokeWidth: 2.0, 
            topLeftRadius: R.dimen.corner_radius, 
            topRightRadius: R.dimen.corner_radius, 
            bottomLeftRadius: R.dimen.corner_radius, 
            bottomRightRadius: R.dimen.corner_radius
        )
    }
}
