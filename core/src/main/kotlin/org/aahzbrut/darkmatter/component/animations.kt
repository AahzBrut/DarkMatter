package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.collections.GdxArray
import org.aahzbrut.darkmatter.DEFAULT_ANIMATION_FRAME_DURATION


enum class AnimationType(
        val atlasKey: String,
        val playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
        val playRate: Float = 1f) {
    NONE(""),
    THRUSTER("player/thruster/Thruster"),
    POWERUP_SHIELD("power_ups/shield/ShieldPowerUp", playRate=.5f),
    POWERUP_SPEED("power_ups/speed/SpeedPowerUp", playRate=.5f),
    POWERUP_TRIPLE_SHOT("power_ups/triple_shot/TripleShotPowerUp", playRate=.5f),
    ENGINE_ON_FIRE("player/engine-on-fire/Fire"),
    ENEMY_EXPLOSION("explosion/Explosion"),
    SHIELD_EFFECT("player/shield/Shields")
}

class Animation2D(
        val type: AnimationType,
        keyFrames: GdxArray<out Sprite>,
        playMode: PlayMode = PlayMode.LOOP,
        playRate: Float = 1f
) : Animation<Sprite>(DEFAULT_ANIMATION_FRAME_DURATION / playRate, keyFrames, playMode)

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