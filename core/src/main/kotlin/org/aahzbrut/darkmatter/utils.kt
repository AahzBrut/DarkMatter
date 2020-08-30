package org.aahzbrut.darkmatter

import com.badlogic.gdx.math.Rectangle
import org.aahzbrut.darkmatter.component.BoundingBoxComponent
import org.aahzbrut.darkmatter.component.TransformComponent

fun Rectangle.set(transform: TransformComponent, boundingBox: BoundingBoxComponent) {
    x = transform.position.x + boundingBox.boundingBox.x
    y = transform.position.y + boundingBox.boundingBox.y
    width = boundingBox.boundingBox.width
    height = boundingBox.boundingBox.height
}