package darkmatter

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool
import darkmatter.component.BoundingBoxComponent
import darkmatter.component.TransformComponent

fun Rectangle.set(transform: TransformComponent, boundingBox: BoundingBoxComponent) {
    x = transform.position.x + boundingBox.boundingBox.x
    y = transform.position.y + boundingBox.boundingBox.y
    width = boundingBox.boundingBox.width
    height = boundingBox.boundingBox.height
}

inline fun <T> Pool<T>.pooled(block: (T) -> Unit) {
    val element = obtain()
    block(element)
    free(element)
}
