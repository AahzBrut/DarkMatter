package darkmatter.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import darkmatter.component.Animation2D
import darkmatter.component.AnimationComponent
import darkmatter.component.AnimationType
import darkmatter.component.GraphicComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import java.util.*


private val LOG = logger<AnimationSystem>()

class AnimationSystem(
        private val atlas: TextureAtlas) :
        IteratingSystem(allOf(AnimationComponent::class).get()), EntityListener {

    private val cache = EnumMap<AnimationType, Animation2D>(AnimationType::class.java)

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        entity[AnimationComponent.mapper]?.let { animComponent ->
            animComponent.animation = getAnimation(animComponent.type)
            val frame = animComponent.animation.getKeyFrame(animComponent.stateTime)
            entity[GraphicComponent.mapper]?.setSpriteRegion(frame)

        }
    }

    private fun getAnimation(type: AnimationType): Animation2D {
        var animation = cache[type]
        if (animation == null) {
            val regions = atlas.findRegions(type.atlasKey)
            if (regions.isEmpty) {
                LOG.error { "Regions with key ${type.atlasKey} was not found in atlas" }
            } else {
                LOG.debug { "Animation of type $type was loaded into cache" }
            }
            animation = Animation2D(type, regions, type.playMode, type.playRate)
            cache[type] = animation
        }
        return animation
    }

    override fun entityRemoved(entity: Entity?) = Unit

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val animComponent = requireNotNull(entity[AnimationComponent.mapper])
        val graphicComponent = requireNotNull(entity[GraphicComponent.mapper])

        if (animComponent.type == AnimationType.NONE)
            return

        if (animComponent.type == animComponent.animation.type) {
            animComponent.stateTime += deltaTime
        } else {
            animComponent.stateTime = 0f
            animComponent.animation = getAnimation(animComponent.type)
        }

        val frame = animComponent.animation.getKeyFrame(animComponent.stateTime)
        graphicComponent.setSpriteRegion(frame)
    }
}