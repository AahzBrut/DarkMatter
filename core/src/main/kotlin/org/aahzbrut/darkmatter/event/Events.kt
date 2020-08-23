package org.aahzbrut.darkmatter.event

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool


sealed class GameEvent : Pool.Poolable

object NonEntity : Entity()

interface GameEventListener<T : GameEvent> {
    fun onEvent(event: T)
}

/**
 * Dummy test event
 */
@Suppress("UNUSED")
data class DummyTestEvent(var player: Entity = NonEntity) : GameEvent() {

    override fun reset() {
        player = NonEntity
    }
}
