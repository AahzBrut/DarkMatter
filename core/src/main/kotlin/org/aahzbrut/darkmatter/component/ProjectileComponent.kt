package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class ProjectileComponent : Component, Pool.Poolable{

    var damage: Float = 0f

    override fun reset() {
        damage = 0f
    }

    companion object {
        val mapper = mapperFor<ProjectileComponent>()
    }
}