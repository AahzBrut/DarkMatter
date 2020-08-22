package darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import darkmatter.ENEMY_BOUNDING_BOX
import darkmatter.ENEMY_SIZE
import darkmatter.ENEMY_SPAWN_DELAY
import darkmatter.ENEMY_SPEED
import darkmatter.WORLD_HEIGHT
import darkmatter.WORLD_WIDTH
import darkmatter.component.BoundingBoxComponent
import darkmatter.component.EnemyComponent
import darkmatter.component.GraphicComponent
import darkmatter.component.MoveComponent
import darkmatter.component.PlayerComponent
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

private val LOG = logger<EnemySystem>()

class EnemySystem(
        private val graphicsAtlas: TextureAtlas) :
        IteratingSystem(
                allOf(
                        EnemyComponent::class,
                        TransformComponent::class,
                        BoundingBoxComponent::class)
                        .exclude(RemoveComponent::class).get()) {

    private val playerBoundingRect = Rectangle()
    private val enemyBoundingRect = Rectangle()
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
        if (timer >= ENEMY_SPAWN_DELAY) {
            spawnEnemy()
            timer = 0f
        }
    }

    private fun spawnEnemy() {
        val entity = engine.entity {
            with<EnemyComponent>{}
            with<TransformComponent> {
                setInitialPosition(getSpawnXPosition(), WORLD_HEIGHT + ENEMY_SIZE, 0f)
                size.set(ENEMY_SIZE, ENEMY_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(ENEMY_BOUNDING_BOX)
            }
            with<MoveComponent> {
                speed.set(0f, -ENEMY_SPEED)
            }
            with<GraphicComponent> {
                setSpriteRegion(graphicsAtlas.findRegion("debris/Asteroid"))
            }
        }

        LOG.debug { "Enemy $entity spawned" }
    }

    private fun getSpawnXPosition() = (0..(WORLD_WIDTH - ENEMY_SIZE).toInt()).random().toFloat()

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val transform = requireNotNull(entity[TransformComponent.mapper])
        val boundingBox = requireNotNull(entity[BoundingBoxComponent.mapper])

        enemyBoundingRect.set(transform, boundingBox)

        checkCollideWithPlayer(entity)

        if (transform.position.y <= -transform.size.y) {
            entity.add(RemoveComponent())
            LOG.debug { "Enemy $entity was marked for removal" }
        }

    }

    private fun checkCollideWithPlayer(entity: Entity) {

        playerEntities.forEach { player ->
            player[TransformComponent.mapper]?.let { transform ->
                player[BoundingBoxComponent.mapper]?.let { boundingBox ->

                    playerBoundingRect.set(transform, boundingBox)

                    if (playerBoundingRect.overlaps(enemyBoundingRect))
                    damagePlayer(player, entity)
                }
            }
        }
    }

    private fun damagePlayer(player: Entity, enemy: Entity) {
        enemy.addComponent<RemoveComponent>(engine)
    }


}