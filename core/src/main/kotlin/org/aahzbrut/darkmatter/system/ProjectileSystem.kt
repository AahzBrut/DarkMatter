package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.logger
import org.aahzbrut.darkmatter.WORLD_HEIGHT
import org.aahzbrut.darkmatter.component.BoundingBoxComponent
import org.aahzbrut.darkmatter.component.ProjectileComponent
import org.aahzbrut.darkmatter.component.RemoveComponent
import org.aahzbrut.darkmatter.component.TransformComponent

@Suppress("UNUSED")
private val LOG by lazy { logger<ProjectileSystem>() }

class ProjectileSystem :
        IteratingSystem(
                allOf(
                        ProjectileComponent::class,
                        TransformComponent::class,
                        BoundingBoxComponent::class)
                        .exclude(RemoveComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = requireNotNull(entity[TransformComponent.mapper])

        if (transform.position.y >= WORLD_HEIGHT + transform.size.y) {
            entity.addComponent<RemoveComponent>(engine)
        }
    }
}