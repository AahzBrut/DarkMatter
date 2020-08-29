package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.logger
import org.aahzbrut.darkmatter.POWER_UP_SPAWN_SCORE
import org.aahzbrut.darkmatter.asset.SoundAsset
import org.aahzbrut.darkmatter.audio.AudioService
import org.aahzbrut.darkmatter.component.*
import org.aahzbrut.darkmatter.factory.PowerUpFactory
import org.aahzbrut.darkmatter.set

@Suppress("UNUSED")
private val LOG = logger<PowerUpSystem>()

class PowerUpSystem (private val audioService: AudioService) :
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

        powerUp.addComponent<RemoveComponent>(engine)

        audioService.play(SoundAsset.POWER_UP)
    }
}