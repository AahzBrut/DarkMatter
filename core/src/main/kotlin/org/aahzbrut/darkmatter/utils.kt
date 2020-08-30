package org.aahzbrut.darkmatter

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import org.aahzbrut.darkmatter.component.BoundingBoxComponent
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.TransformComponent

fun Rectangle.set(transform: TransformComponent, boundingBox: BoundingBoxComponent) {
    x = transform.position.x + boundingBox.boundingBox.x
    y = transform.position.y + boundingBox.boundingBox.y
    width = boundingBox.boundingBox.width
    height = boundingBox.boundingBox.height
}

fun playerScore(player: Entity, score: Int) {
    player[PlayerComponent.mapper]?.let {
        it.score += score
        it.enemiesKilled++
    }
}