package org.aahzbrut.darkmatter.screen

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import org.aahzbrut.darkmatter.DarkMatter

abstract class BaseScreen(
        val game: DarkMatter,
        val gameViewport: Viewport = game.gameViewport,
        private val uiViewport: Viewport = game.uiViewport,
        val assetStorage : AssetStorage =  game.assetStorage,
        val stage: Stage = game.stage
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
        game.rayHandler.useCustomViewport(gameViewport.screenX, gameViewport.screenY, gameViewport.screenWidth, gameViewport.screenHeight)
    }
}