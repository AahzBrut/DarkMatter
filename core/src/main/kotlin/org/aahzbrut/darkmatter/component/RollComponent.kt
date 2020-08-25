package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class RollComponent: Component, Pool.Poolable {

    var rollAmount = 0f

    override fun reset() {
        rollAmount = 0f
    }

    companion object {
        val mapper = mapperFor<RollComponent>()
    }
}