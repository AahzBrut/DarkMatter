package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.component.RemoveComponent

private val LOG = logger<RemoveSystem>()

class RemoveSystem : IteratingSystem(
        allOf(RemoveComponent::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removeComponent = requireNotNull(entity[RemoveComponent.mapper])

        removeComponent.delay -= deltaTime
        if (removeComponent.delay <= 0f) {
            LOG.debug { "Entity $entity was removed" }
            engine.removeEntity(entity)
        }
    }
}