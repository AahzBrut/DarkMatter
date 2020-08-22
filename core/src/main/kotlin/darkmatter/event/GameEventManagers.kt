package darkmatter.event

import kotlin.reflect.KClass

object GameEventManagers {

    private val eventManagers = mutableMapOf<KClass<*>, GameEventManager<*>>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T : GameEvent> get(type: KClass<T>) =
            eventManagers[type] as GameEventManager<T>?
                    ?: GameEventManager(type).also { eventManagers[type] = it }
}