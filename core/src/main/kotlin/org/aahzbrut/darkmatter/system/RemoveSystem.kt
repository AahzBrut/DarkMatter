package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import org.aahzbrut.darkmatter.component.RemoveComponent

@Suppress("UNUSED")
private val LOG = logger<RemoveSystem>()

class RemoveSystem : IteratingSystem(
        allOf(RemoveComponent::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removeComponent = requireNotNull(entity[RemoveComponent.mapper])

        removeComponent.delay -= deltaTime
        if (removeComponent.delay <= 0f) {
            engine.removeEntity(entity)
        }
    }
}