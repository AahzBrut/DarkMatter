package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class BoundingBoxComponent : Component, Pool.Poolable {

    val boundingBox = Rectangle()

    override fun reset() {
        boundingBox.set(0f,0f,0f,0f)
    }

    companion object {
        val mapper = mapperFor<BoundingBoxComponent>()
    }
}