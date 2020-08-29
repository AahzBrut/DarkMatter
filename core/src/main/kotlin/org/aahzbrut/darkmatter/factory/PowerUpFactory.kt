package org.aahzbrut.darkmatter.factory

import com.badlogic.ashley.core.Engine
import ktx.ashley.entity
import ktx.ashley.with
import org.aahzbrut.darkmatter.*
import org.aahzbrut.darkmatter.component.*

class PowerUpFactory(private val engine: Engine) {

    fun spawn(powerUpType: PowerUpType) {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(getSpawnXPosition(), WORLD_HEIGHT + POWERUP_SIZE, 0f)
                size.set(POWERUP_SIZE, POWERUP_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(POWERUP_BOUNDING_BOX)
            }
            with<MoveComponent> {
                velocity.set(0f, -POWERUP_SPEED)
            }
            with<PowerUpComponent> {
                type = powerUpType
            }
            with<AnimationComponent> {
                type = powerUpType.animationType
            }
            with<GraphicComponent> {}
        }
    }

    private fun getSpawnXPosition() = (0..(WORLD_WIDTH - POWERUP_SIZE).toInt()).random().toFloat()
}