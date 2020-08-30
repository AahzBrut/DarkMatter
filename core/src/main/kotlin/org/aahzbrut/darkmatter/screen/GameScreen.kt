package org.aahzbrut.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.FPSLogger
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.DarkMatter
import org.aahzbrut.darkmatter.MAX_DELTA_TIME
import org.aahzbrut.darkmatter.PLAYER_SIZE
import org.aahzbrut.darkmatter.asset.MusicAsset
import org.aahzbrut.darkmatter.factory.BackgroundFactory
import org.aahzbrut.darkmatter.factory.PlayerFactory
import kotlin.math.min


private val LOG = logger<GameScreen>()
private var fpsLogger = FPSLogger()

class GameScreen(
        game: DarkMatter,
        val engine: Engine = game.engine) :
        BaseScreen(game) {

    private val backgroundFactory by lazy {
        BackgroundFactory(engine, game.spriteCache)
    }

    private val playerFactory by lazy {
        PlayerFactory(engine, game.spriteCache)
    }

    override fun show() {
        LOG.debug { "First screen is showing" }

        game.audioService.play(MusicAsset.BACKGROUND_MUSIC, 1f)

        backgroundFactory.spawn()
        playerFactory.spawn((gameViewport.worldWidth - PLAYER_SIZE) / 2, 1f, 0f)
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
        game.audioService.update()
        // fpsLogger.log()
    }

    override fun dispose() {
        LOG.debug { "Disposing resources" }
    }
}