package darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class RollComponent: Component, Pool.Poolable {

    var horizontalDirection = RollDirection.DEFAULT
    var verticalDirection = VerticalDirection.DEFAULT

    override fun reset() {
        horizontalDirection = RollDirection.DEFAULT
        verticalDirection = VerticalDirection.DEFAULT
    }

    companion object {
        val mapper = mapperFor<RollComponent>()
    }
}

enum class RollDirection {
    LEFT, DEFAULT, RIGHT
}

enum class VerticalDirection {
    UP, DEFAULT, DOWN
}