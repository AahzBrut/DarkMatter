package org.aahzbrut.darkmatter.system

import box2dLight.RayHandler
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.ashley.allOf
import ktx.ashley.get
import org.aahzbrut.darkmatter.component.LightComponent
import org.aahzbrut.darkmatter.component.TransformComponent

class LightSystem(
        private val rayHandler: RayHandler,
        private val camera: OrthographicCamera) : EntitySystem() {

    override fun update(deltaTime: Float) {
        updateLightPositions()
        rayHandler.setCombinedMatrix(camera)
        rayHandler.updateAndRender()
    }

    private fun updateLightPositions() {
        engine.getEntitiesFor(allOf(TransformComponent::class, LightComponent::class).get()).forEach { entity ->
            val transform = entity[TransformComponent.mapper]!!
            val light = entity[LightComponent.mapper]!!
            light.light.setPosition(transform.interpolatedPosition.x + transform.size.x * .5f, transform.interpolatedPosition.y + transform.size.y * .5f)
            Gdx.app.log("Update light", "update light")
        }
    }
}