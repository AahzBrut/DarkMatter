package darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.MathUtils.lerp
import darkmatter.HOR_ACCELERATION
import darkmatter.MAX_HOR_SPEED
import darkmatter.MAX_VER_SPEED
import darkmatter.UPDATE_RATE
import darkmatter.VER_ACCELERATION
import darkmatter.WORLD_HEIGHT
import darkmatter.WORLD_WIDTH
import darkmatter.component.MoveComponent
import darkmatter.component.PlayerComponent
import darkmatter.component.RemoveComponent
import darkmatter.component.RollComponent
import darkmatter.component.RollDirection
import darkmatter.component.TransformComponent
import darkmatter.component.VerticalDirection
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
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

            updatePrevPosition()

            super.update(UPDATE_RATE)
        }
        updateInterpolatedPosition()
    }

    private fun updateInterpolatedPosition() {
        val alfa = timeSinceLastUpdate / UPDATE_RATE
        entities.forEach { entity ->
            entity[TransformComponent.mapper]?.let { transform ->
                transform.interpolatedPosition.set(
                        lerp(transform.prevPosition.x, transform.position.x, alfa),
                        lerp(transform.prevPosition.y, transform.position.y, alfa),
                        transform.position.z
                )
            }
        }
    }

    private fun updatePrevPosition() {
        entities.forEach { entity ->
            entity[TransformComponent.mapper]?.let { transform ->
                transform.prevPosition.set(transform.position)
            }
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

        moveEntity(transform, move, deltaTime, true)
    }


    private fun moveEntity(transform: TransformComponent, move: MoveComponent, deltaTime: Float, clamp: Boolean = false) {

        transform.position.x = if (clamp)
            clamp(
                    transform.position.x + move.speed.x * deltaTime,
                    0f,
                    WORLD_WIDTH - transform.size.x)
        else
            transform.position.x + move.speed.x * deltaTime

        transform.position.y = if (clamp)
            clamp(
                    transform.position.y + move.speed.y * deltaTime,
                    0f,
                    WORLD_HEIGHT - transform.size.y)
        else
            transform.position.y + move.speed.y * deltaTime
    }
}