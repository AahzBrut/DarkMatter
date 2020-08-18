package darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import darkmatter.component.RemoveComponent
import ktx.ashley.allOf
import ktx.ashley.get

class RemoveSystem : IteratingSystem(
        allOf(RemoveComponent::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removeComponent = requireNotNull(entity[RemoveComponent.mapper])

        removeComponent.delay -= deltaTime
        if (removeComponent.delay <= 0f)
            engine.removeEntity(entity)
    }
}