package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import org.aahzbrut.darkmatter.MAX_WEAPON_DELAY
import org.aahzbrut.darkmatter.asset.SoundAsset
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.audio.AudioService
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.RemoveComponent
import org.aahzbrut.darkmatter.component.TransformComponent
import org.aahzbrut.darkmatter.component.WeaponComponent
import org.aahzbrut.darkmatter.factory.ProjectileFactory

@Suppress("UNUSED")
private val LOG = logger<WeaponSystem>()

class WeaponSystem(private val spriteCache: SpriteCache,
                   private val audioService: AudioService) :
        IteratingSystem(
                allOf(
                        PlayerComponent::class,
                        TransformComponent::class,
                        WeaponComponent::class
                )
                        .exclude(RemoveComponent::class.java)
                        .get()) {

    private val projectileFactory by lazy {
        ProjectileFactory(engine, spriteCache)
    }

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

        projectileFactory.spawn(
                transform.position.x + weapon.mainGunPosition.x,
                transform.position.y + weapon.mainGunPosition.y,
                0f)

        audioService.play(SoundAsset.SHOT, .4f)
    }
}