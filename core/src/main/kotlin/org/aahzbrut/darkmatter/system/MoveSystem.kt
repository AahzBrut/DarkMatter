package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils.clamp
import com.badlogic.gdx.math.MathUtils.lerp
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktx.log.logger
import org.aahzbrut.darkmatter.MAX_ACCELERATION
import org.aahzbrut.darkmatter.MAX_SPEED
import org.aahzbrut.darkmatter.PLAYER_DISTANCE_TO_TARGET_ALLOWANCE
import org.aahzbrut.darkmatter.PLAYER_ROLL_MAX_VALUE
import org.aahzbrut.darkmatter.PLAYER_ROLL_SPEED
import org.aahzbrut.darkmatter.PLAYER_ROLL_TOLERANCE
import org.aahzbrut.darkmatter.UPDATE_RATE
import org.aahzbrut.darkmatter.WORLD_HEIGHT
import org.aahzbrut.darkmatter.WORLD_WIDTH
import org.aahzbrut.darkmatter.component.MoveComponent
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.RemoveComponent
import org.aahzbrut.darkmatter.component.RollComponent
import org.aahzbrut.darkmatter.component.TransformComponent
import kotlin.math.abs
import kotlin.math.sign

@Suppress("UNUSED")
private val LOG = logger<MoveSystem>()

class MoveSystem :
        IteratingSystem(
                allOf(
                        TransformComponent::class,
                        MoveComponent::class)
                        .exclude(
                                RemoveComponent::class)
                        .get()) {

    private var timeSinceLastUpdate = 0f
    private val vectorToTarget = Vector2()
    private val directionToTarget = Vector2()
    private var newSpeed = 0f

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
            updateRoll(entity, transformComponent, moveComponent, deltaTime)
            movePlayer(transformComponent, moveComponent, deltaTime)
        } else {
            moveEntity(transformComponent, moveComponent, deltaTime)
        }

    }

    private fun updateRoll(player: Entity, transform: TransformComponent, move: MoveComponent, deltaTime: Float) {
        vectorToTarget.set(move.target.x - transform.position.x - transform.size.x / 2, move.target.y - transform.position.y - transform.size.y / 2)
        val roll = requireNotNull(player[RollComponent.mapper])
        if (abs(vectorToTarget.x) < PLAYER_ROLL_TOLERANCE) {
            // return to center
            if (abs(roll.rollAmount) < PLAYER_ROLL_SPEED * deltaTime) {
                roll.rollAmount = 0f
            } else {
                roll.rollAmount -= sign(roll.rollAmount) * (PLAYER_ROLL_SPEED * deltaTime)
            }
        } else {
            // roll
            roll.rollAmount += sign(vectorToTarget.x) * (PLAYER_ROLL_SPEED * deltaTime)
            roll.rollAmount = clamp(roll.rollAmount, -PLAYER_ROLL_MAX_VALUE.toFloat(), PLAYER_ROLL_MAX_VALUE.toFloat())
        }
    }

    private fun movePlayer(transform: TransformComponent, move: MoveComponent, deltaTime: Float) {
        vectorToTarget.set(move.target.x - transform.position.x - transform.size.x / 2, move.target.y - transform.position.y - transform.size.y / 2)
        val distanceToTarget = vectorToTarget.len()
        if (distanceToTarget <= PLAYER_DISTANCE_TO_TARGET_ALLOWANCE) return

        vectorToTarget.nor()
        directionToTarget.set(vectorToTarget)

        newSpeed = move.velocity.len() + MAX_ACCELERATION * deltaTime
        newSpeed = clamp(newSpeed, -MAX_SPEED, MAX_SPEED)
        move.velocity.set(vectorToTarget.scl(newSpeed))

        //LOG.debug { "Target: ${move.target}, position: ${transform.position}, velocity: ${move.velocity}, distance:$distanceToTarget" }

        // clamp speed to not overshoot target
        if (distanceToTarget < move.velocity.len() * deltaTime) {
            move.velocity.set(directionToTarget.scl(distanceToTarget * deltaTime))
        }

        moveEntity(transform, move, deltaTime, true)
    }


    private fun moveEntity(transform: TransformComponent, move: MoveComponent, deltaTime: Float, clamp: Boolean = false) {

        transform.position.x = if (clamp)
            clamp(
                    transform.position.x + move.velocity.x * deltaTime,
                    0f,
                    WORLD_WIDTH - transform.size.x)
        else
            transform.position.x + move.velocity.x * deltaTime

        transform.position.y = if (clamp)
            clamp(
                    transform.position.y + move.velocity.y * deltaTime,
                    0f,
                    WORLD_HEIGHT - transform.size.y)
        else
            transform.position.y + move.velocity.y * deltaTime
    }
}