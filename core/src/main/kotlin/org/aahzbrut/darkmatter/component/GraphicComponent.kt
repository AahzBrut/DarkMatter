package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

object EmptySprite: Sprite()

class GraphicComponent: Component, Pool.Poolable {

    var sprite: Sprite = EmptySprite

    override fun reset() {
        sprite = EmptySprite
    }

    fun resetSprite(sprite: Sprite) {
        this.sprite = sprite
    }

    companion object {
        val mapper = mapperFor<GraphicComponent>()
    }
}