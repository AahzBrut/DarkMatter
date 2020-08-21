package darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor


enum class PowerUpType(val animationType: AnimationType) {
    NONE(AnimationType.NONE),
    POWERUP_SHIELD(AnimationType.POWERUP_SHIELD),
    POWERUP_SPEED(AnimationType.POWERUP_SPEED),
    POWERUP_TRIPLE_SHOT(AnimationType.POWERUP_TRIPLE_SHOT)
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