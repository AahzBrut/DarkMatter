package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import org.aahzbrut.darkmatter.TOUCH_TOLERANCE_DISTANCE
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.RollComponent
import org.aahzbrut.darkmatter.component.RollDirection
import org.aahzbrut.darkmatter.component.TransformComponent
import org.aahzbrut.darkmatter.component.VerticalDirection

private val LOG = logger<PlayerInputSystem>()

class PlayerInputSystem(
        private val gameViewport: Viewport
) : IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class, RollComponent::class).get()) {

    private var rollVector: Vector2 = Vector2.Zero

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val roll = requireNotNull(entity[RollComponent.mapper])
        val transform = requireNotNull(entity[TransformComponent.mapper])

        rollVector = gameViewport.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()))

        val diffX = rollVector.x - transform.position.x - transform.size.x * .5
        val diffY = rollVector.y - transform.position.y - transform.size.y * .5

        roll.horizontalDirection = when {
            diffX < -TOUCH_TOLERANCE_DISTANCE -> RollDirection.LEFT
            diffX > TOUCH_TOLERANCE_DISTANCE -> RollDirection.RIGHT
            else -> RollDirection.DEFAULT
        }

        roll.verticalDirection = when {
            diffY < -TOUCH_TOLERANCE_DISTANCE -> VerticalDirection.DOWN
            diffY > TOUCH_TOLERANCE_DISTANCE -> VerticalDirection.UP
            else -> VerticalDirection.DEFAULT
        }
  }
}