package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

@Suppress("UNUSED")
enum class PowerUpType(val animationType: AnimationType) {
    NONE(AnimationType.NONE),
    POWERUP_SHIELD(AnimationType.POWERUP_SHIELD),
    POWERUP_SPEED(AnimationType.POWERUP_SPEED),
    POWERUP_TRIPLE_SHOT(AnimationType.POWERUP_TRIPLE_SHOT);

    companion object {

        private var prevPowerUp = 0

        fun getRandom(): PowerUpType {
            var value = prevPowerUp
            while (value == prevPowerUp) value = (1..3).random()
            prevPowerUp = value
            return values()[value]
        }
    }
}

class PowerUpComponent : Component, Pool.Poolable {

    var type = PowerUpType.NONE

    override fun reset() {
        type = PowerUpType.NONE
    }
    
    companion object {
        val mapper = mapperFor<PowerUpComponent>()
    }
}