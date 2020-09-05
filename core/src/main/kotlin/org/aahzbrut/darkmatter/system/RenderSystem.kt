package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.has
import ktx.graphics.use
import ktx.log.error
import ktx.log.logger
import org.aahzbrut.darkmatter.component.GraphicComponent
import org.aahzbrut.darkmatter.component.ShockWaveComponent
import org.aahzbrut.darkmatter.component.TransformComponent

private val LOG = logger<RenderSystem>()

class RenderSystem(
        private val batch: Batch,
        private val gameViewport: Viewport,
        private val shockWaveShader: ShaderProgram
) : SortedIteratingSystem(
        allOf(TransformComponent::class, GraphicComponent::class).get(),
        compareBy { entity -> entity[TransformComponent.mapper] }
) {

    private val shaderTime = shockWaveShader.getUniformLocation("time")

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = requireNotNull(entity[TransformComponent.mapper])
        val graphic = requireNotNull(entity[GraphicComponent.mapper])

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


    // Disabled for now - works on the texture of all asteroids, need to use fbo to correct this
    private fun renderShockWaves(deltaTime: Float) {
        batch.use(gameViewport.camera.combined){
            it.shader = shockWaveShader
            entities
                    .filter { entity -> entity.has(ShockWaveComponent.mapper) }
                    .forEach {entity ->
                        val swComponent = entity[ShockWaveComponent.mapper]!!
                        val graphic = entity[GraphicComponent.mapper]!!

                        graphic.sprite.run {
                            shockWaveShader.setUniformf(shaderTime, swComponent.timeFromStart)
                            draw(it)
                        }
                        swComponent.timeFromStart += deltaTime
                    }

            it.shader = null
        }
    }

    override fun update(deltaTime: Float) {
        drawGame(deltaTime)
    }

    private fun drawGame(deltaTime: Float) {
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            super.update(deltaTime)
        }
        //renderShockWaves(deltaTime)
    }


}