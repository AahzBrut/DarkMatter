package org.aahzbrut.darkmatter.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import org.aahzbrut.darkmatter.MAX_PLAYER_LIVES

class PlayerComponent : Component, Pool.Poolable {

    var numLives = MAX_PLAYER_LIVES
    var enemiesKilled = 0
    var enemiesLost = 0
    var score = 0
    var time = 0

    override fun reset() {
        numLives = MAX_PLAYER_LIVES
        enemiesKilled = 0
        enemiesLost = 0
        score = 0
        time = 0
    }

    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }
}