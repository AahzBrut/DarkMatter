package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class ShockWaveComponent: Component, Pool.Poolable {

    var timeFromStart = 0f

    override fun reset() {
        timeFromStart = 0f
    }

    companion object {
        val mapper = mapperFor<ShockWaveComponent>()
    }
}