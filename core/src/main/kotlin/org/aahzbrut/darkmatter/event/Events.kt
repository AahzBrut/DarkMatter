package org.aahzbrut.darkmatter.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool


sealed class GameEvent : Pool.Poolable

object NonEntity : Entity()

interface GameEventListener<T : GameEvent> {
    fun onEvent(event: T)
}

data class PlayerDamageEvent(
        var player: Entity = NonEntity,
        var numLivesLeft: Int = 0
) : GameEvent() {

    override fun reset() {
        player = NonEntity
        numLivesLeft = 0
    }
}