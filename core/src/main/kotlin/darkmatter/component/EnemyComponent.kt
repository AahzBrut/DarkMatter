package darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

/**
 * Marker component for enemies
 */
class EnemyComponent: Component, Pool.Poolable {

    override fun reset() = Unit
}