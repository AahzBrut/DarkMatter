package darkmatter

import com.badlogic.gdx.math.Rectangle
import darkmatter.component.TransformComponent

fun Rectangle.set(transform: TransformComponent) {
    x = transform.position.x
    y = transform.position.y
    width = transform.size.x
    height = transform.size.y
}