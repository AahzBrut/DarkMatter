package darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class RollComponent: Component, Pool.Poolable {

    var rollDirection = RollDirection.DEFAULT

    override fun reset() {
        rollDirection = RollDirection.DEFAULT
    }

    companion object {
        val mapper = mapperFor<RollComponent>()
    }
}

enum class RollDirection {
    LEFT, DEFAULT, RIGHT
}