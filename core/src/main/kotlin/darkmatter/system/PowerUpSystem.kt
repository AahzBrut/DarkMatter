package darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import darkmatter.PLAYER_SIZE
import darkmatter.POWERUP_BOUNDING_BOX
import darkmatter.POWERUP_SIZE
import darkmatter.POWERUP_SPEED
import darkmatter.WORLD_HEIGHT
import darkmatter.WORLD_WIDTH
import darkmatter.component.AnimationComponent
import darkmatter.component.AnimationType
import darkmatter.component.BoundingBoxComponent
import darkmatter.component.GraphicComponent
import darkmatter.component.MoveComponent
import darkmatter.component.PlayerComponent
import darkmatter.component.PowerUpComponent
import darkmatter.component.RemoveComponent
import darkmatter.component.TransformComponent
import darkmatter.set
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger


private val LOG = logger<PowerUpSystem>()

class PowerUpSystem :
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

    private var timer = 0f

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        timer += deltaTime
        if (timer >= 2f) {
            spawnPowerUp()
            timer = 0f
        }

    }

    private fun spawnPowerUp() {
        val entity = engine.entity {
            with<TransformComponent> {
                setInitialPosition(WORLD_WIDTH / 2, WORLD_HEIGHT + PLAYER_SIZE, 0f)
                size.set(POWERUP_SIZE, POWERUP_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(POWERUP_BOUNDING_BOX)
            }
            with<MoveComponent> {
                speed.set(0f, -POWERUP_SPEED)
            }
            with<PowerUpComponent> {}
            with<AnimationComponent> {
                type = AnimationType.POWERUP_SHIELD
            }
            with<GraphicComponent> {}
        }

        LOG.debug { "PowerUp $entity spawned" }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val transform = requireNotNull(entity[TransformComponent.mapper])
        val boundingBox = requireNotNull(entity[BoundingBoxComponent.mapper])

        powerUpBoundingRect.set(transform, boundingBox)

        checkCollideWithPlayer(entity)

        if (transform.position.y <= -transform.size.y) {
            entity.add(RemoveComponent())
            LOG.debug { "Entity $entity was marked for removal" }
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
    }
}