package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.EmptySprite
import org.aahzbrut.darkmatter.component.GraphicComponent
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.RollComponent
import org.aahzbrut.darkmatter.component.RollDirection


private val LOG = logger<PlayerAnimationSystem>()

class PlayerAnimationSystem(spriteCache: SpriteCache) :
        IteratingSystem(
                allOf(
                        PlayerComponent::class,
                        RollComponent::class,
                        GraphicComponent::class)
                        .get()),
        EntityListener {

    private var lastRoll = RollDirection.DEFAULT

    private val textureRegions = mapOf(
            RollDirection.DEFAULT to spriteCache.getSprite("player/player"),
            RollDirection.LEFT to spriteCache.getSprite("player/turn-left/TurnLeft", 8),
            RollDirection.RIGHT to spriteCache.getSprite("player/turn-right/TurnRight", 8)
    )

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val rollComponent = requireNotNull(entity[RollComponent.mapper])
        val graphicComponent = requireNotNull(entity[GraphicComponent.mapper])

        if (lastRoll == rollComponent.horizontalDirection && graphicComponent.sprite != EmptySprite) return

        graphicComponent.resetSprite(textureRegions[rollComponent.horizontalDirection]
                ?: throw RuntimeException("Texture regions are not set"))
        lastRoll = rollComponent.horizontalDirection
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
        entity[GraphicComponent.mapper]?.resetSprite(textureRegions[RollDirection.DEFAULT] ?: error("Texture regions map not initialized"))
    }

    override fun entityRemoved(entity: Entity) = Unit
}