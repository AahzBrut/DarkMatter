package org.aahzbrut.darkmatter.factory

import box2dLight.PointLight
import box2dLight.RayHandler
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import ktx.ashley.entity
import ktx.ashley.with
import org.aahzbrut.darkmatter.MAX_RAY_NUMBER
import org.aahzbrut.darkmatter.PLAYER_BOUNDING_BOX
import org.aahzbrut.darkmatter.PLAYER_SIZE
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.*

class PlayerFactory(private val engine: Engine,
                    private val spriteCache: SpriteCache,
                    private val rayHandler: RayHandler) {

    fun spawn(x: Float, y: Float, z: Float) {
        val player = engine.entity {
            with<TransformComponent> {
                setInitialPosition(x, y, z)
                size.set(PLAYER_SIZE, PLAYER_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(PLAYER_BOUNDING_BOX)
            }
            with<MoveComponent> {}
            with<GraphicComponent> {}
            with<PlayerComponent> {}
            with<RollComponent> {}
            with<WeaponComponent> {
                mainGunPosition.set(1.75f, 3.5f)
                leftGunPosition.set(.5f, 1f)
                rightGunPosition.set(PLAYER_SIZE - 1f, 1f)
            }
            with<LightComponent> {
                light = PointLight(rayHandler, MAX_RAY_NUMBER)
                light.color = Color(1f,1f,1f,.6f)
                light.distance = 8f
                light.isActive = true
                light.isXray = true
                light.isStaticLight = true
            }
        }

        engine.entity {
            with<TransformComponent> { size.set(PLAYER_SIZE, PLAYER_SIZE) }
            with<AnimationComponent> {
                type = AnimationType.THRUSTER
            }
            with<GraphicComponent> {}
            with<AttachmentComponent> {
                entity = player
                offset.set(0f, -3f)
            }
        }

    }
}