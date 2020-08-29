package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.POWER_UP_SHIELD_MAX_ACTIVE_TIME
import org.aahzbrut.darkmatter.POWER_UP_SPEED_MAX_ACTIVE_TIME
import org.aahzbrut.darkmatter.POWER_UP_TRIPLE_SHOT_MAX_ACTIVE_TIME
import org.aahzbrut.darkmatter.component.*

@Suppress("UNUSED")
private val LOG = logger<PowerUpEffectSystem>()

class PowerUpEffectSystem :
        IteratingSystem(allOf(
                PlayerComponent::class
        ).get()) {

    override fun processEntity(player: Entity, deltaTime: Float) {
        val shieldComponent = player[ShieldComponent.mapper]
        val tripleShotComponent = player[TripleShotComponent.mapper]
        val speedComponent = player[SpeedComponent.mapper]

        shieldComponent?.let {
            it.activeTime += deltaTime
            if (it.activeTime >= POWER_UP_SHIELD_MAX_ACTIVE_TIME) {
                player.remove(ShieldComponent::class.java)
                LOG.debug { "Shield effect removed" }
                engine.getEntitiesFor(
                        allOf(ShieldVFXComponent::class)
                                .exclude(RemoveComponent::class)
                                .get())
                        .forEach { shieldVFX ->
                            shieldVFX.addComponent<RemoveComponent>(engine)
                        }
            }
        }

        tripleShotComponent?.let {
            it.activeTime += deltaTime
            if (it.activeTime >= POWER_UP_TRIPLE_SHOT_MAX_ACTIVE_TIME) {
                player.remove(TripleShotComponent::class.java)
                LOG.debug { "Triple shot effect removed" }
            }
        }

        speedComponent?.let {
            it.activeTime += deltaTime
            if (it.activeTime >= POWER_UP_SPEED_MAX_ACTIVE_TIME) {
                player.remove(SpeedComponent::class.java)
                LOG.debug { "Speed effect removed" }
            }
        }
    }
}