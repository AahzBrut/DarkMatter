package org.aahzbrut.darkmatter.event

import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import ktx.collections.GdxSet
import ktx.log.logger
import kotlin.reflect.KClass

class GameEventManager<T : GameEvent>(type: KClass<T>) {

    val listeners = GdxSet<(T) -> Unit>()
    val eventPool: Pool<T> = Pools.get(type.java)

    fun addEventListener(listener: (T) -> Unit) {
        listeners.add(listener)
    }

    fun removeEventListener(listener: (T) -> Unit) {
        listeners.remove(listener)
    }

    inline fun dispatchEvent(block: T.() -> Unit) {
        eventPool.pooled { event ->
            event.block()
            listeners.forEach { it(event) }
        }
    }

    companion object {
        val LOG = logger<GameEventManager<*>>()
    }
}

inline fun <T> Pool<T>.pooled(block: (T) -> Unit) {
    val element = obtain()
    block(element)
    free(element)
}