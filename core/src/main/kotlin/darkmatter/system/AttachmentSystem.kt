package darkmatter.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import darkmatter.component.AttachmentComponent
import darkmatter.component.GraphicComponent
import darkmatter.component.RemoveComponent
import darkmatter.component.TransformComponent
import ktx.ashley.addComponent
import ktx.ashley.allOf
import ktx.ashley.get

class AttachmentSystem :
        IteratingSystem(
                allOf(
                        AttachmentComponent::class,
                        TransformComponent::class,
                        GraphicComponent::class
                ).get()),
        EntityListener {

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) = Unit

    override fun entityRemoved(removedEntity: Entity) {
        entities.forEach { entity ->
            entity[AttachmentComponent.mapper]?.let { attach ->
                if (attach.entity == removedEntity)
                    entity.addComponent<RemoveComponent>(engine)

            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val attach = requireNotNull(entity[AttachmentComponent.mapper])
        updatePosition(entity, attach)
        updateAlfa(entity, attach)
    }

    private fun updateAlfa(entity: Entity, attach: AttachmentComponent) {
        val graphics = requireNotNull(entity[GraphicComponent.mapper])
        attach.entity[GraphicComponent.mapper]?.let { attachGraphics ->
            graphics.sprite.setAlpha(attachGraphics.sprite.color.a)

        }
    }

    private fun updatePosition(entity: Entity, attach: AttachmentComponent): AttachmentComponent {
        val transform = requireNotNull(entity[TransformComponent.mapper])

        attach.entity[TransformComponent.mapper]?.let { attachTransform ->
            transform.interpolatedPosition.set(
                    attachTransform.interpolatedPosition.x + attach.offset.x,
                    attachTransform.interpolatedPosition.y + attach.offset.y,
                    transform.position.z
            )
        }
        return attach
    }
}