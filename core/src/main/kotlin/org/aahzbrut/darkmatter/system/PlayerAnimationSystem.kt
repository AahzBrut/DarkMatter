package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Sprite
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.collections.GdxArray
import ktx.log.logger
import org.aahzbrut.darkmatter.PLAYER_ROLL_MAX_VALUE
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.EmptySprite
import org.aahzbrut.darkmatter.component.GraphicComponent
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.RollComponent

@Suppress("UNUSED")
private val LOG = logger<PlayerAnimationSystem>()

class PlayerAnimationSystem(spriteCache: SpriteCache) :
        IteratingSystem(
                allOf(
                        PlayerComponent::class,
                        RollComponent::class,
                        GraphicComponent::class)
                        .get()),
        EntityListener {

    private val sprites by lazy {
        val spriteArray = GdxArray<Sprite>(2 * PLAYER_ROLL_MAX_VALUE + 1)
        spriteArray.also {
            spriteCache.getSprites("player/turn-left/TurnLeft").reverse()
            it.addAll(spriteCache.getSprites("player/turn-left/TurnLeft"))
            it.addAll(spriteCache.getSprites("player/player"))
            it.addAll(spriteCache.getSprites("player/turn-right/TurnRight"))
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val rollComponent = requireNotNull(entity[RollComponent.mapper])
        val graphicComponent = requireNotNull(entity[GraphicComponent.mapper])

        if (rollComponent.rollAmount == 0f && graphicComponent.sprite != EmptySprite) return

        graphicComponent.resetSprite(sprites[rollComponent.rollAmount.toInt() + PLAYER_ROLL_MAX_VALUE]
                ?: throw RuntimeException("Texture regions are not set"))
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        entity[GraphicComponent.mapper]?.resetSprite(sprites[PLAYER_ROLL_MAX_VALUE]
                ?: error("Texture regions map not initialized"))
    }

    override fun entityRemoved(entity: Entity) = Unit
}

