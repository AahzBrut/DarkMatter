package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import org.aahzbrut.darkmatter.component.BoundingBoxComponent
import org.aahzbrut.darkmatter.component.TransformComponent

class BoundingBoxRenderingSystem(
        private val viewport: Viewport,
        private val shapeRenderer: ShapeRenderer
) :
        IteratingSystem(
                allOf(
                        BoundingBoxComponent::class,
                        TransformComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = requireNotNull(entity[TransformComponent.mapper])

        entity[BoundingBoxComponent.mapper]?.let { bbox ->
            shapeRenderer.rect(transform.position.x + bbox.boundingBox.x, transform.position.y + bbox.boundingBox.y, bbox.boundingBox.width, bbox.boundingBox.height)
        }
    }

    override fun update(deltaTime: Float) {
        shapeRenderer.projectionMatrix = viewport.camera.combined
        shapeRenderer.use(ShapeRenderer.ShapeType.Line) {
            super.update(deltaTime)
        }
    }
}