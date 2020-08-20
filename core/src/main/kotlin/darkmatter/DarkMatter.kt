package darkmatter

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.FitViewport
import darkmatter.screen.BaseScreen
import darkmatter.screen.FirstScreen
import darkmatter.screen.SecondScreen
import darkmatter.system.*
import ktx.app.KtxGame
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<DarkMatter>()

class DarkMatter : KtxGame<BaseScreen>() {

    private val defaultTextureRegion by lazy { TextureRegion(Texture(Gdx.files.internal("sprites/player.png"))) }
    private val leftTextureRegion by lazy { TextureRegion(Texture(Gdx.files.internal("sprites/Player Turn Left0008.png"))) }
    private val rightTextureRegion by lazy { TextureRegion(Texture(Gdx.files.internal("sprites/Player Turn Right0008.png"))) }

    private val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal("graphics/EngineAnimation.atlas"))}

    val gameViewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)
    private val batch: Batch by lazy { SpriteBatch() }
    val engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(PlayerAnimationSystem(
                    defaultTextureRegion,
                    leftTextureRegion,
                    rightTextureRegion
            ))
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(RenderSystem(batch, gameViewport))
            addSystem(RemoveSystem())
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
        defaultTextureRegion.texture.dispose()
        leftTextureRegion.texture.dispose()
        rightTextureRegion.texture.dispose()
        batch.dispose()
    }
}