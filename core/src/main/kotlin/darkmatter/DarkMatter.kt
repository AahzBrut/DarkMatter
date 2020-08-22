package darkmatter

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import darkmatter.screen.BaseScreen
import darkmatter.screen.FirstScreen
import darkmatter.screen.SecondScreen
import darkmatter.system.AnimationSystem
import darkmatter.system.AttachmentSystem
import darkmatter.system.BoundingBoxRenderingSystem
import darkmatter.system.EnemySystem
import darkmatter.system.MoveSystem
import darkmatter.system.PlayerAnimationSystem
import darkmatter.system.PlayerInputSystem
import darkmatter.system.PowerUpSystem
import darkmatter.system.RemoveSystem
import darkmatter.system.RenderSystem
import ktx.app.KtxGame
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<DarkMatter>()

class DarkMatter : KtxGame<BaseScreen>() {

    private val defaultTextureRegion: TextureRegion by lazy { graphicsAtlas.findRegion("player/player") }
    private val leftTextureRegion: TextureRegion by lazy { graphicsAtlas.findRegion("player/turn-left/TurnLeft", 8) }
    private val rightTextureRegion: TextureRegion by lazy { graphicsAtlas.findRegion("player/turn-right/TurnRight", 8) }

    val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal("graphics/textures.atlas")) }

    val gameViewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)
    private val batch: Batch by lazy { SpriteBatch() }
    private val shapeRenderer by lazy {
        val shapeRenderer = ShapeRenderer()
        shapeRenderer.projectionMatrix = gameViewport.camera.combined
        shapeRenderer
    }

    val engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(PowerUpSystem())
            addSystem(EnemySystem(graphicsAtlas))
            addSystem(PlayerAnimationSystem(
                    defaultTextureRegion,
                    leftTextureRegion,
                    rightTextureRegion
            ))
            addSystem(AttachmentSystem())
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(RenderSystem(batch, gameViewport))
            addSystem(BoundingBoxRenderingSystem(gameViewport, shapeRenderer))
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
        graphicsAtlas.dispose()
        batch.dispose()
    }
}