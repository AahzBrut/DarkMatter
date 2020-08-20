package darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Pool
import darkmatter.DEFAULT_ANIMATION_FRAME_DURATION
import ktx.ashley.mapperFor


enum class AnimationType(
        val atlasKey: String,
        val playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
        val playRate: Float = 1f) {
    NONE(""),
    THRUSTER("Thruster")
}

class Animation2D(
        val type: AnimationType,
        keyFrames: Array<out TextureRegion>,
        playMode: PlayMode = PlayMode.LOOP,
        playRate: Float = 1f
) : Animation<TextureRegion>(DEFAULT_ANIMATION_FRAME_DURATION / playRate, keyFrames, playMode)

class AnimationComponent : Component, Pool.Poolable {

    var type = AnimationType.NONE
    var stateTime = 0f
    lateinit var animation: Animation2D

    override fun reset() {
        type = AnimationType.NONE
        stateTime = 0f
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
    }
}