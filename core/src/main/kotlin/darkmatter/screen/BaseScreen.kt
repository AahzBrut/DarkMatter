package darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.Viewport
import darkmatter.DarkMatter
import ktx.app.KtxScreen

abstract class BaseScreen(
        val game: DarkMatter,
        val gameViewport: Viewport = game.gameViewport,
        val engine: Engine = game.engine,
        val graphicsAtlas: TextureAtlas = game.graphicsAtlas
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
    }
}