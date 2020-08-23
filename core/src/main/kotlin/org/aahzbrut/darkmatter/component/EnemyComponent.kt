package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

/**
 * Marker component for enemies
 */
class EnemyComponent: Component, Pool.Poolable {

    override fun reset() = Unit

    companion object {
        val mapper = mapperFor<EnemyComponent>()
    }
}