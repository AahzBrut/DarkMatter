package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import ktx.ashley.*
import ktx.log.logger
import org.aahzbrut.darkmatter.*
import org.aahzbrut.darkmatter.asset.SoundAsset
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.audio.AudioService
import org.aahzbrut.darkmatter.component.*
import org.aahzbrut.darkmatter.event.GameEventManagers
import org.aahzbrut.darkmatter.event.ScoreEvent
import org.aahzbrut.darkmatter.factory.EnemyFactory

@Suppress("UNUSED")
private val LOG = logger<EnemySystem>()

object EmptyEntityArray : ImmutableArray<Entity>(Array())

class EnemySystem(
        private val spriteCache: SpriteCache,
        private val audioService: AudioService) :
        IteratingSystem(
                allOf(
                        EnemyComponent::class,
                        TransformComponent::class,
                        BoundingBoxComponent::class)
                        .exclude(RemoveComponent::class).get()) {

    private val scoreEventManager = GameEventManagers[ScoreEvent::class]
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
    private val enemyFactory by lazy {
        EnemyFactory(engine, spriteCache)
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
        enemyFactory.spawn()
    }

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
            destroyEnemy(enemy, false)

            playerEntities.forEach {
                playerScore(it, ENEMY_ESCAPE_SCORE)
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
                                destroyEnemy(enemy, true)
                                projectile.addComponent<RemoveComponent>(engine)
                                playerEntities.forEach {
                                    playerScore(it, ENEMY_KILL_SCORE)
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
        destroyEnemy(enemy, true)

        player[PlayerComponent.mapper]?.let {
            it.score += ENEMY_KILL_SCORE
            it.enemiesKilled++
            scoreEventManager.dispatchEvent {
                numLivesLeft = it.numLives
                score = it.score
            }

            if (player.has(ShieldComponent.mapper)) return

            scoreEventManager.dispatchEvent {
                this.numLivesLeft = --it.numLives
                this.score = it.score
            }

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

    private fun destroyEnemy(enemy: Entity, withExplosion: Boolean) {
        if (withExplosion) {
            enemy.addComponent<RemoveComponent>(engine) {
                delay = 3f
            }
            enemy.addComponent<AnimationComponent>(engine) {
                type = AnimationType.ENEMY_EXPLOSION
            }
            audioService.play(SoundAsset.EXPLOSION, pitch = MathUtils.random(.5f, 2f))
        } else {
            enemy.addComponent<RemoveComponent>(engine)
        }
    }

    private fun playerScore(player: Entity, score: Int) {
        player[PlayerComponent.mapper]?.let {
            it.score += score
            it.enemiesKilled++
            scoreEventManager.dispatchEvent {
                this.numLivesLeft = it.numLives
                this.score = it.score
            }
        }
    }
}