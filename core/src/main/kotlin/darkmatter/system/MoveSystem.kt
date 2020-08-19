package darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.clamp
import darkmatter.*
import darkmatter.component.*
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger
import kotlin.math.max
import kotlin.math.min


private val LOG = logger<MoveSystem>()

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
            entity[RollComponent.mapper]?.let {
                movePlayer(transformComponent, moveComponent, it, deltaTime)
            }
        } else {
            moveEntity(transformComponent, moveComponent, deltaTime)
        }
    }

    private fun movePlayer(transform: TransformComponent, move: MoveComponent, roll: RollComponent, deltaTime: Float) {
        val horizontalSpeed = when (roll.horizontalDirection) {
            RollDirection.LEFT -> min(0f, move.speed.x - HOR_ACCELERATION * deltaTime)
            RollDirection.RIGHT -> max(0f, move.speed.x + HOR_ACCELERATION * deltaTime)
            else -> 0f
        }

        val verticalSpeed = when (roll.verticalDirection) {
            VerticalDirection.DOWN -> min(0f, move.speed.y - VER_ACCELERATION * deltaTime)
            VerticalDirection.UP -> max(0f, move.speed.y + VER_ACCELERATION * deltaTime)
            else -> 0f
        }

        move.speed.x = clamp(horizontalSpeed, -MAX_HOR_SPEED, MAX_HOR_SPEED)
        move.speed.y = clamp(verticalSpeed, -MAX_VER_SPEED, MAX_VER_SPEED)

        moveEntity(transform, move, deltaTime)
    }


    private fun moveEntity(transform: TransformComponent, move: MoveComponent, deltaTime: Float) {
        transform.position.x = clamp(
                transform.position.x + move.speed.x * deltaTime,
                0f,
                WORLD_WIDTH - transform.size.x)
        transform.position.y = clamp(
                transform.position.y + move.speed.y * deltaTime,
                0f,
                WORLD_HEIGHT - transform.size.y)
    }
}