package org.aahzbrut.darkmatter.component

import box2dLight.Light
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class LightComponent: Component, Pool.Poolable {

    lateinit var light: Light

    override fun reset() {
        light.remove()
    }

    companion object {
        val mapper = mapperFor<LightComponent>()
    }
}