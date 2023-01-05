import XmlToXibRuntime
public extension R.drawable {
    static func launch_background() -> CALayer {
        LayerMaker.autosize(
                .init(layer: LayerMaker.rect(
                    fillColor: R.color.background, 
                    strokeColor: .clear, 
                    strokeWidth: 0, 
                    topLeftRadius: 0, 
                    topRightRadius: 0, 
                    bottomLeftRadius: 0, 
                    bottomRightRadius: 0
                )
            , insets: .init(top: 0, left: 0, bottom: 0, right: 0), scaleOverResize: false), 
                .init(layer: R.drawable.logo_title()
        , insets: .init(top: 100.0, left: 100.0, bottom: 100.0, right: 100.0), scaleOverResize: true))
    }
}
