package org.aahzbrut.darkmatter.screen

import com.badlogic.gdx.utils.viewport.Viewport
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import org.aahzbrut.darkmatter.DarkMatter

abstract class BaseScreen(
        val game: DarkMatter,
        val gameViewport: Viewport = game.gameViewport,
        val assetStorage : AssetStorage =  game.assetStorage
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
    }
}