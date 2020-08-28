package org.aahzbrut.darkmatter.factory

import com.badlogic.ashley.core.Engine
import ktx.ashley.entity
import ktx.ashley.with
import org.aahzbrut.darkmatter.*
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.*

class EnemyFactory(private val engine: Engine,
                   private val spriteCache: SpriteCache) {

    fun spawn() {

        engine.entity {
            with<EnemyComponent> {}
            with<TransformComponent> {
                setInitialPosition(getSpawnXPosition(), WORLD_HEIGHT + ENEMY_SIZE, 0f)
                size.set(ENEMY_SIZE, ENEMY_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(ENEMY_BOUNDING_BOX)
            }
            with<MoveComponent> {
                velocity.set(0f, -ENEMY_SPEED)
            }
            with<GraphicComponent> {
                resetSprite(spriteCache.getSprite("debris/Asteroid"))
            }
        }
    }

    private fun getSpawnXPosition() = (0..(WORLD_WIDTH - ENEMY_SIZE).toInt()).random().toFloat()
}