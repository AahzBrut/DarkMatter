package org.aahzbrut.darkmatter.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool


sealed class GameEvent : Pool.Poolable

@Suppress("UNUSED")
object NonEntity : Entity()

data class PlayerDamageEvent(
        var numLivesLeft: Int = 0
) : GameEvent() {

    override fun reset() {
        numLivesLeft = 0
    }
}

data class ScoreEvent(
        var score: Int = 0
) : GameEvent() {

    override fun reset() {
        score = 0
    }
}