package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.POWER_UP_SPAWN_SCORE
import org.aahzbrut.darkmatter.asset.SoundAsset
import org.aahzbrut.darkmatter.audio.AudioService
import org.aahzbrut.darkmatter.component.*
import org.aahzbrut.darkmatter.factory.PowerUpFactory
import org.aahzbrut.darkmatter.factory.ShieldEffectFactory
import org.aahzbrut.darkmatter.set

@Suppress("UNUSED")
private val LOG = logger<PowerUpSystem>()

class PowerUpSystem(private val audioService: AudioService) :
        IteratingSystem(
                allOf(
                        PowerUpComponent::class,
                        TransformComponent::class,
                        BoundingBoxComponent::class)
                        .exclude(RemoveComponent::class).get()) {

    private val playerBoundingRect = Rectangle()
    private val powerUpBoundingRect = Rectangle()
    private val playerEntities by lazy {
        engine.getEntitiesFor(
                allOf(PlayerComponent::class,
                        BoundingBoxComponent::class)
                        .exclude(RemoveComponent::class).get()
        )
    }
    private val powerUpFactory by lazy {
        PowerUpFactory(engine)
    }

    private val shieldEffectFactory by lazy {
        ShieldEffectFactory(engine)
    }

    private var lastScore = 0f

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        playerEntities.forEach { player ->
            player[PlayerComponent.mapper]?.let {
                if (it.score - lastScore >= POWER_UP_SPAWN_SCORE) {
                    powerUpFactory.spawn(PowerUpType.getRandom())
                    lastScore += POWER_UP_SPAWN_SCORE
                }
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val transform = requireNotNull(entity[TransformComponent.mapper])
        val boundingBox = requireNotNull(entity[BoundingBoxComponent.mapper])

        powerUpBoundingRect.set(transform, boundingBox)

        checkCollideWithPlayer(entity)

        if (transform.position.y <= -transform.size.y) {
            entity.addComponent<RemoveComponent>(engine)
        }
    }

    private fun checkCollideWithPlayer(entity: Entity) {

        playerEntities.forEach { player ->
            player[TransformComponent.mapper]?.let { transform ->
                player[BoundingBoxComponent.mapper]?.let { boundingBox ->

                    playerBoundingRect.set(transform, boundingBox)

                    if (playerBoundingRect.overlaps(powerUpBoundingRect))
                        collectPowerUp(player, entity)
                }
            }
        }
    }

    private fun collectPowerUp(player: Entity, powerUp: Entity) {
        val powerUpComponent = requireNotNull(powerUp[PowerUpComponent.mapper])

        // add power up to player
        when (powerUpComponent.type) {
            PowerUpType.NONE -> Unit
            PowerUpType.POWERUP_SHIELD -> {
                if (player.has(ShieldComponent.mapper)) {
                    player[ShieldComponent.mapper]?.activeTime = 0f
                } else {
                    player.addComponent<ShieldComponent>(engine)
                    shieldEffectFactory.spawn(player)
                }
                LOG.debug { "Shield effect added" }
            }
            PowerUpType.POWERUP_SPEED -> {
                if (player.has(SpeedComponent.mapper)) {
                    player[SpeedComponent.mapper]?.activeTime = 0f
                } else {
                    player.addComponent<SpeedComponent>(engine)
                }
                LOG.debug { "Speed effect added" }
            }
            PowerUpType.POWERUP_TRIPLE_SHOT -> {
                if (player.has(TripleShotComponent.mapper)) {
                    player[TripleShotComponent.mapper]?.activeTime = 0f
                } else {
                    player.addComponent<TripleShotComponent>(engine)
                }
                LOG.debug { "Triple shot effect added" }
            }
        }

        powerUp.addComponent<RemoveComponent>(engine)

        audioService.play(SoundAsset.POWER_UP)
    }
}