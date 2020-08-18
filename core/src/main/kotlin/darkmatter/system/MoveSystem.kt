package darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import darkmatter.UPDATE_RATE
import darkmatter.WORLD_HEIGHT
import darkmatter.WORLD_WIDTH
import darkmatter.component.MoveComponent
import darkmatter.component.PlayerComponent
import darkmatter.component.RemoveComponent
import darkmatter.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get

class MoveSystem :
        IteratingSystem(allOf(TransformComponent::class, MoveComponent::class).exclude(RemoveComponent::class).get()) {

    private var timeSinceLastUpdate = 0f

    override fun update(deltaTime: Float) {
        timeSinceLastUpdate += deltaTime
        while (timeSinceLastUpdate >= UPDATE_RATE) {
            timeSinceLastUpdate -= UPDATE_RATE
            super.update(UPDATE_RATE)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformComponent = requireNotNull(entity[TransformComponent.mapper])
        val moveComponent = requireNotNull(entity[MoveComponent.mapper])
        val player = entity[PlayerComponent.mapper]

        if (player != null) {

        } else {
            moveEntity(transformComponent, moveComponent, deltaTime)
        }
    }

    private fun moveEntity(transform: TransformComponent, move: MoveComponent, deltaTime: Float) {
        transform.position.x = MathUtils.clamp(
                transform.position.x + move.speed.x * deltaTime,
                0f,
                WORLD_WIDTH - transform.size.x)
        transform.position.y = MathUtils.clamp(
                transform.position.y + move.speed.y * deltaTime,
                0f,
                WORLD_HEIGHT - transform.size.y)
    }
}