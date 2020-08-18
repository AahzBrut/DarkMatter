package darkmatter.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import darkmatter.component.GraphicComponent
import darkmatter.component.PlayerComponent
import darkmatter.component.RollComponent
import darkmatter.component.RollDirection
import ktx.ashley.allOf
import ktx.ashley.get


class PlayerAnimationSystem(
        private val defaultRegion: TextureRegion,
        leftRegion: TextureRegion,
        rightRegion: TextureRegion
) : IteratingSystem(allOf(PlayerComponent::class, RollComponent::class, GraphicComponent::class).get()), EntityListener {

    private var lastRoll = RollDirection.DEFAULT

    private val textureRegions = mapOf(
            RollDirection.DEFAULT to defaultRegion,
            RollDirection.LEFT to leftRegion,
            RollDirection.RIGHT to rightRegion
    )

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val rollComponent = requireNotNull(entity[RollComponent.mapper])
        val graphicComponent = requireNotNull(entity[GraphicComponent.mapper])

        if (lastRoll == rollComponent.rollDirection && graphicComponent.sprite.texture != null) return

        graphicComponent.setSpriteRegion(textureRegions[rollComponent.rollDirection] ?: throw RuntimeException("Texture regions are not set"))
        lastRoll = rollComponent.rollDirection
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
        entity[GraphicComponent.mapper]?.setSpriteRegion(defaultRegion)
    }

    override fun entityRemoved(entity: Entity) = Unit
}