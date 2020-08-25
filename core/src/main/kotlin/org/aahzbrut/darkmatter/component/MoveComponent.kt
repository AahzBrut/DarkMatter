package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class MoveComponent: Component, Pool.Poolable {

    val velocity = Vector2()
    val target = Vector2()

    override fun reset() {
        velocity.set(Vector2.Zero)
        target.set(Vector2.Zero)
    }

    companion object {
        val mapper = mapperFor<MoveComponent>()
    }
}