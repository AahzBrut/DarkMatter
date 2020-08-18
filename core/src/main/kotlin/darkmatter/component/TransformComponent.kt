package darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.get
import ktx.ashley.mapperFor

class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {

    val position = Vector3()
    val size = Vector2(1f, 1f)
    var rotation = 0f

    override fun compareTo(other: TransformComponent): Int {

        return (position.z - other.position.z).toInt()
    }

    override fun reset() {
        position.set(Vector3.Zero)
        size.set(1f, 1f)
        rotation = 0f
    }

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }
}