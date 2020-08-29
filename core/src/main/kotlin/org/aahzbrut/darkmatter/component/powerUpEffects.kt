package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

abstract class PowerUpEffectComponent : Component, Pool.Poolable {

    var activeTime = 0f

    override fun reset() {
        activeTime = 0f
    }
}

class ShieldComponent : PowerUpEffectComponent() {

    companion object {
        val mapper = mapperFor<ShieldComponent>()
    }
}

class TripleShotComponent : PowerUpEffectComponent() {

    companion object {
        val mapper = mapperFor<TripleShotComponent>()
    }
}

class SpeedComponent : PowerUpEffectComponent() {

    companion object {
        val mapper = mapperFor<SpeedComponent>()
    }
}

class ShieldVFXComponent : Component, Pool.Poolable {

    override fun reset() = Unit

    companion object {
        val mapper = mapperFor<ShieldVFXComponent>()
    }
}