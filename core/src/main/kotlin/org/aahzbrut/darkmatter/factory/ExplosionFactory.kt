package org.aahzbrut.darkmatter.factory

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import ktx.ashley.entity
import ktx.ashley.with
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.*

@Suppress("UNUSED")
class ExplosionFactory(private val engine: Engine,
                       private val spriteCache: SpriteCache) {

    fun spawn(position: Vector3, size: Vector2, speed: Vector2, acceleration: Vector2) {

        engine.entity {
            with<TransformComponent> {
                this.setInitialPosition(position)
                this.size.set(size)
            }
            with<MoveComponent> {
                this.velocity.set(speed)
                this.acceleration.set(acceleration)
            }
            with<AnimationComponent> {
                type = AnimationType.ENEMY_EXPLOSION
            }
            with<GraphicComponent> {
                resetSprite(spriteCache.getSprite("debris/Asteroid"))
            }
            with<RemoveComponent> {
                delay = 3f
            }
        }
    }

}