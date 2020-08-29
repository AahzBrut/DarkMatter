package org.aahzbrut.darkmatter.factory

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import ktx.ashley.entity
import ktx.ashley.with
import org.aahzbrut.darkmatter.PLAYER_SIZE
import org.aahzbrut.darkmatter.component.*

class ShieldEffectFactory(private val engine: Engine) {

    fun spawn(entity: Entity) {
        engine.entity {
            with<TransformComponent> { size.set(PLAYER_SIZE * 1.5f, PLAYER_SIZE * 1.5f) }
            with<AnimationComponent> {
                type = AnimationType.SHIELD_EFFECT
            }
            with<GraphicComponent> {}
            with<AttachmentComponent> {
                this.entity = entity
                offset.set(-1f, -1f)
            }
            with<ShieldVFXComponent> {}
        }

    }
}