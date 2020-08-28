package org.aahzbrut.darkmatter.factory

import com.badlogic.ashley.core.Engine
import ktx.ashley.entity
import ktx.ashley.with
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.*

class ProjectileFactory(private val engine: Engine,
                        private val spriteCache: SpriteCache) {

    fun spawn(x: Float, y: Float, z: Float) {

        engine.entity {
            with<TransformComponent> {
                setInitialPosition(x, y, z)
                size.set(org.aahzbrut.darkmatter.PROJECTILE_SIZE / 4f, org.aahzbrut.darkmatter.PROJECTILE_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(org.aahzbrut.darkmatter.PROJECTILE_BOUNDING_BOX)
            }
            with<MoveComponent> {
                velocity.set(0f, org.aahzbrut.darkmatter.PROJECTILE_SPEED)
            }
            with<GraphicComponent> {
                resetSprite(spriteCache.getSprite("projectiles/laser"))
            }
            with<ProjectileComponent> {}
        }

    }

}