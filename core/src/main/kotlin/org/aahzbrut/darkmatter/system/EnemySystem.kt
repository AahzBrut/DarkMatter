package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.logger
import org.aahzbrut.darkmatter.ENEMY_BOUNDING_BOX
import org.aahzbrut.darkmatter.ENEMY_SIZE
import org.aahzbrut.darkmatter.ENEMY_SPAWN_DELAY
import org.aahzbrut.darkmatter.ENEMY_SPEED
import org.aahzbrut.darkmatter.WORLD_HEIGHT
import org.aahzbrut.darkmatter.WORLD_WIDTH
import org.aahzbrut.darkmatter.component.BoundingBoxComponent
import org.aahzbrut.darkmatter.component.EnemyComponent
import org.aahzbrut.darkmatter.component.GraphicComponent
import org.aahzbrut.darkmatter.component.MoveComponent
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.RemoveComponent
import org.aahzbrut.darkmatter.component.TransformComponent
import org.aahzbrut.darkmatter.set

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
            repeat((timer / ENEMY_SPAWN_DELAY).toInt()) {
                spawnEnemy()
            }
            timer = 0f
        }
    }

    private fun spawnEnemy() {
        val entity = engine.entity {
            with<EnemyComponent> {}
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
    }

    private fun getSpawnXPosition() = (0..(WORLD_WIDTH - ENEMY_SIZE).toInt()).random().toFloat()

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val transform = requireNotNull(entity[TransformComponent.mapper])
        val boundingBox = requireNotNull(entity[BoundingBoxComponent.mapper])

        enemyBoundingRect.set(transform, boundingBox)

        checkCollideWithPlayer(entity)

        if (transform.position.y <= -transform.size.y) {
            entity.add(RemoveComponent())
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