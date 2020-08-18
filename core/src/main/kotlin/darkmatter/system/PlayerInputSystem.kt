package darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import darkmatter.TOUCH_TOLERANCE_DISTANCE
import darkmatter.component.PlayerComponent
import darkmatter.component.RollComponent
import darkmatter.component.RollDirection
import darkmatter.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<PlayerInputSystem>()

class PlayerInputSystem(
        private val gameViewport: Viewport
) : IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class, RollComponent::class).get()) {

    private var rollVector: Vector2 = Vector2.Zero

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val roll = requireNotNull(entity[RollComponent.mapper])
        val transform = requireNotNull(entity[TransformComponent.mapper])

        rollVector = gameViewport.unproject(Vector2(Gdx.input.x.toFloat(), 0f))

        val diffX = rollVector.x - transform.position.x - transform.size.x * .5

        roll.rollDirection = when {
            diffX < -TOUCH_TOLERANCE_DISTANCE -> RollDirection.LEFT
            diffX > TOUCH_TOLERANCE_DISTANCE -> RollDirection.RIGHT
            else -> RollDirection.DEFAULT
        }
  }
}