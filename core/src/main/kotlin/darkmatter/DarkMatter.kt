package darkmatter

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import darkmatter.screen.BaseScreen
import darkmatter.screen.FirstScreen
import darkmatter.screen.SecondScreen
import darkmatter.system.RenderSystem
import ktx.app.KtxGame
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<DarkMatter>()

class DarkMatter : KtxGame<BaseScreen>() {

    val gameViewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)
    private val batch: Batch by lazy { SpriteBatch() }
    val engine by lazy {
        PooledEngine().apply {
            addSystem(RenderSystem(batch, gameViewport))
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        LOG.debug { "IN: DarkMatter::create()" }
        addScreen(FirstScreen(this))
        addScreen(SecondScreen(this))
        setScreen<FirstScreen>()
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Disposing resources" }
        batch.dispose()
    }
}