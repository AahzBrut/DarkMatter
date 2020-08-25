package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.logger
import org.aahzbrut.darkmatter.component.MoveComponent
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.TransformComponent

@Suppress("UNUSED")
private val LOG = logger<PlayerInputSystem>()

class PlayerInputSystem(
        private val gameViewport: Viewport
) : IteratingSystem(
        allOf(
                PlayerComponent::class,
                MoveComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val movement = requireNotNull(entity[MoveComponent.mapper])

        movement.target.set(gameViewport.unproject(Vector2(
                Gdx.input.x.toFloat(),
                Gdx.input.y.toFloat())))
    }
}