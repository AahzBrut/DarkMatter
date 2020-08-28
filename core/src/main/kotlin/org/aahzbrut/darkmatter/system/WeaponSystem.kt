package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.logger
import org.aahzbrut.darkmatter.MAX_WEAPON_DELAY
import org.aahzbrut.darkmatter.PROJECTILE_BOUNDING_BOX
import org.aahzbrut.darkmatter.PROJECTILE_SIZE
import org.aahzbrut.darkmatter.PROJECTILE_SPEED
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.*

@Suppress("UNUSED")
private val LOG = logger<WeaponSystem>()

class WeaponSystem(private val spriteCache: SpriteCache) :
        IteratingSystem(
                allOf(
                        PlayerComponent::class,
                        TransformComponent::class,
                        WeaponComponent::class
                )
                        .exclude(RemoveComponent::class.java)
                        .get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val weapon = requireNotNull(entity[WeaponComponent.mapper])

        weapon.lastShotFired += deltaTime
        if (weapon.lastShotFired >= MAX_WEAPON_DELAY) {
            spawnProjectile(entity, weapon)
            weapon.lastShotFired = 0f
        }
    }

    private fun spawnProjectile(entity: Entity, weapon: WeaponComponent) {
        val transform = requireNotNull(entity[TransformComponent.mapper])

        engine.entity {
            with<TransformComponent> {
                setInitialPosition(
                        transform.position.x + weapon.mainGunPosition.x,
                        transform.position.y + weapon.mainGunPosition.y,
                        0f)
                size.set(PROJECTILE_SIZE / 4f, PROJECTILE_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(PROJECTILE_BOUNDING_BOX)
            }
            with<MoveComponent> {
                velocity.set(0f, PROJECTILE_SPEED)
            }
            with<GraphicComponent> {
                resetSprite(spriteCache.getSprite("projectiles/laser"))
            }
            with<ProjectileComponent> {}
        }
    }
}