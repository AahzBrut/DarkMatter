package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class WeaponComponent : Component, Pool.Poolable {

    val mainGunPosition = Vector2()
    val leftGunPosition = Vector2()
    val rightGunPosition  = Vector2()
    var lastShotFired = 0f

    override fun reset() {
        mainGunPosition.set(Vector2.Zero)
        leftGunPosition.set(Vector2.Zero)
        rightGunPosition.set(Vector2.Zero)
        lastShotFired = 0f
    }

    companion object{
        val mapper = mapperFor<WeaponComponent>()
    }
}