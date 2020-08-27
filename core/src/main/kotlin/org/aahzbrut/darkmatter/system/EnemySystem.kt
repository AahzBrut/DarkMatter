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
import org.aahzbrut.darkmatter.*
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.*

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
        checkEnemyEscaped(transform, entity)
    }

    private fun checkEnemyEscaped(transform: TransformComponent, enemy: Entity) {
        if (transform.position.y <= -transform.size.y) {
            enemy.addComponent<RemoveComponent>(engine)
            playerEntities.forEach { player ->
                player[PlayerComponent.mapper]?.let {
                    it.score += ENEMY_ESCAPE_SCORE
                    it.enemiesLost++
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
                                playerEntities.forEach { player ->
                                    player[PlayerComponent.mapper]?.let {
                                        it.score += ENEMY_KILL_SCORE
                                        it.enemiesKilled++
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
            it.numLives--
            it.enemiesKilled++
            LOG.debug { "Score: ${it.score}" }

            if (it.numLives > 0) {
                addEngineOnFireAnimation(player, it.numLives)
            } else {
                destroyPlayer(player)
            }
        }


    }

    private fun destroyPlayer(player: Entity) {
        val attachments = engine.getEntitiesFor(
                allOf(AttachmentComponent::class)
                        .exclude(RemoveComponent::class).get())

        attachments.forEach {
            val attachment = requireNotNull(it[AttachmentComponent.mapper])
            if (attachment.entity == player)
                it.addComponent<RemoveComponent>(engine)
        }

        player.addComponent<RemoveComponent>(engine)
    }

    private fun addEngineOnFireAnimation(player: Entity, numLives: Int) {

        engine.entity {
            with<TransformComponent> { size.set(PLAYER_SIZE, PLAYER_SIZE) }
            with<AnimationComponent> {
                type = AnimationType.ENGINE_ON_FIRE
            }
            with<GraphicComponent> {}
            with<AttachmentComponent> {
                entity = player
                if (numLives == 1)
                    offset.set(-1f, -3f)
                else
                    offset.set(1f, -3f)
            }

        }
    }
}