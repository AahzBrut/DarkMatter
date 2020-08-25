package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.ENEMY_BOUNDING_BOX
import org.aahzbrut.darkmatter.ENEMY_ESCAPE_SCORE
import org.aahzbrut.darkmatter.ENEMY_KILL_SCORE
import org.aahzbrut.darkmatter.ENEMY_SIZE
import org.aahzbrut.darkmatter.ENEMY_SPAWN_DELAY
import org.aahzbrut.darkmatter.ENEMY_SPAWN_DELAY_DELTA
import org.aahzbrut.darkmatter.ENEMY_SPEED
import org.aahzbrut.darkmatter.WORLD_HEIGHT
import org.aahzbrut.darkmatter.WORLD_WIDTH
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.BoundingBoxComponent
import org.aahzbrut.darkmatter.component.EnemyComponent
import org.aahzbrut.darkmatter.component.GraphicComponent
import org.aahzbrut.darkmatter.component.MoveComponent
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.ProjectileComponent
import org.aahzbrut.darkmatter.component.RemoveComponent
import org.aahzbrut.darkmatter.component.TransformComponent
import org.aahzbrut.darkmatter.set

@Suppress("UNUSED")
private val LOG = logger<EnemySystem>()

object EmptyEntityArray : ImmutableArray<Entity>(Array())

class EnemySystem(
        private val spriteCache: SpriteCache) :
        IteratingSystem(
                allOf(
                        EnemyComponent::class,
                        TransformComponent::class,
                        BoundingBoxComponent::class)
                        .exclude(RemoveComponent::class).get()) {

    private val playerBoundingRect = Rectangle()
    private val enemyBoundingRect = Rectangle()
    private val projectileBoundingRect = Rectangle()
    private val playerEntities by lazy {
        engine.getEntitiesFor(
                allOf(PlayerComponent::class,
                        BoundingBoxComponent::class)
                        .exclude(RemoveComponent::class).get()
        )
    }
    private var projectiles: ImmutableArray<Entity> = EmptyEntityArray

    private var timer = 0f
    private var spawnAccelTimer = 0f
    private var spawnDelay = ENEMY_SPAWN_DELAY

    override fun update(deltaTime: Float) {
        cacheProjectiles()
        super.update(deltaTime)

        timer += deltaTime
        while (timer >= spawnDelay) {
            spawnEnemy()
            timer -= spawnDelay
        }

        spawnAccelTimer += deltaTime
        while (spawnAccelTimer >= 1f) {
            spawnDelay *= ENEMY_SPAWN_DELAY_DELTA
            spawnAccelTimer -= 1f
        }
    }

    private fun spawnEnemy() {
        engine.entity {
            with<EnemyComponent> {}
            with<TransformComponent> {
                setInitialPosition(getSpawnXPosition(), WORLD_HEIGHT + ENEMY_SIZE, 0f)
                size.set(ENEMY_SIZE, ENEMY_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(ENEMY_BOUNDING_BOX)
            }
            with<MoveComponent> {
                velocity.set(0f, -ENEMY_SPEED)
            }
            with<GraphicComponent> {
                resetSprite(spriteCache.getSprite("debris/Asteroid"))
            }
        }
    }

    private fun getSpawnXPosition() = (0..(WORLD_WIDTH - ENEMY_SIZE).toInt()).random().toFloat()

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val transform = requireNotNull(entity[TransformComponent.mapper])
        val boundingBox = requireNotNull(entity[BoundingBoxComponent.mapper])

        enemyBoundingRect.set(transform, boundingBox)

        checkCollideWithPlayer(entity)
        checkCollideWithProjectile(entity)

        if (transform.position.y <= -transform.size.y) {
            entity.addComponent<RemoveComponent>(engine)
            playerEntities.forEach { player ->
                player[PlayerComponent.mapper]?.let {
                    it.score += ENEMY_ESCAPE_SCORE
                    LOG.debug { "Score: ${it.score}" }
                }
            }
        }

    }

    private fun checkCollideWithProjectile(enemy: Entity) {
        projectiles
                .filter { it[RemoveComponent.mapper] == null }
                .forEach { projectile ->
                    projectile[TransformComponent.mapper]?.let { transform ->
                        projectile[BoundingBoxComponent.mapper]?.let { boundingBox ->

                            projectileBoundingRect.set(transform, boundingBox)

                            if (projectileBoundingRect.overlaps(enemyBoundingRect)) {
                                enemy.addComponent<RemoveComponent>(engine)
                                projectile.addComponent<RemoveComponent>(engine)
                                playerEntities.forEach {player->
                                    player[PlayerComponent.mapper]?.let {
                                        it.score += ENEMY_KILL_SCORE
                                        LOG.debug { "Score: ${it.score}" }
                                    }
                                }
                            }
                        }
                    }
                }
    }

    private fun cacheProjectiles() {
        projectiles = engine.getEntitiesFor(
                allOf(ProjectileComponent::class,
                        TransformComponent::class,
                        BoundingBoxComponent::class)
                        .exclude(RemoveComponent::class).get())
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
        player[PlayerComponent.mapper]?.let {
            it.score += ENEMY_KILL_SCORE
            LOG.debug { "Score: ${it.score}" }
        }
    }


}