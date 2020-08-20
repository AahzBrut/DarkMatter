package darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import darkmatter.component.GraphicComponent
import darkmatter.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.error
import ktx.log.logger

private val LOG = logger<RenderSystem>()

class RenderSystem(
        private val batch: Batch,
        private val gameViewport: Viewport
): SortedIteratingSystem(
        allOf(TransformComponent::class, GraphicComponent::class).get(),
        compareBy {entity -> entity[TransformComponent.mapper]}
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = requireNotNull(entity[TransformComponent.mapper])
        val graphic  = requireNotNull(entity[GraphicComponent.mapper])

        if (graphic.sprite.texture == null) {
            LOG.error { "Entity $entity is missing texture." }
            return
        }

        graphic.sprite.run {
            rotation = transform.rotation
            setBounds(transform.interpolatedPosition.x, transform.interpolatedPosition.y, transform.size.x, transform.size.y)
            draw(batch)
        }
    }

    override fun update(deltaTime: Float) {
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            super.update(deltaTime)
        }
    }
}