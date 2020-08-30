package org.aahzbrut.darkmatter.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool


sealed class GameEvent : Pool.Poolable

@Suppress("UNUSED")
object NonEntity : Entity()

interface GameEventListener<T : GameEvent> {
    fun onEvent(event: T)
}

data class ScoreEvent(
        var numLivesLeft: Int = 0,
        var score: Int = 0
) : GameEvent() {

    override fun reset() {
        numLivesLeft = 0
        score = 0
    }
}