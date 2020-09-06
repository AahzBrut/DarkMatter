package org.aahzbrut.darkmatter.factory

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import ktx.ashley.entity
import ktx.ashley.with
import org.aahzbrut.darkmatter.MAX_RAY_NUMBER
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.*

class ProjectileFactory(private val engine: Engine,
                        private val spriteCache: SpriteCache,
                        private val rayHandler: RayHandler) {
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
            with<LightComponent> {
                light = PointLight(rayHandler, MAX_RAY_NUMBER)
                light.color = Color(.8f,0f,0f,.6f)
                light.distance = 4f
                light.isActive = true
                light.isXray = true
                light.isStaticLight = true
            }

        }

    }

}