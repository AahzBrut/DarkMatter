package org.aahzbrut.darkmatter.factory

import com.badlogic.ashley.core.Engine
import ktx.ashley.entity
import ktx.ashley.with
import org.aahzbrut.darkmatter.WORLD_HEIGHT
import org.aahzbrut.darkmatter.WORLD_WIDTH
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.component.GraphicComponent
import org.aahzbrut.darkmatter.component.TransformComponent

class BackgroundFactory(private val engine: Engine,
                        private val spriteCache: SpriteCache) {

    fun spawn() {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(0f, 0f, -1f)
                size.set(WORLD_WIDTH, WORLD_HEIGHT)
            }
            with<GraphicComponent> {
                resetSprite(spriteCache.getSprite("background/SpaceBG_Overlay"))
            }
        }
    }
}