package org.aahzbrut.darkmatter

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.asset.TextureAtlasAsset
import org.aahzbrut.darkmatter.audio.AudioService
import org.aahzbrut.darkmatter.audio.DefaultAudioService
import org.aahzbrut.darkmatter.screen.BaseScreen
import org.aahzbrut.darkmatter.screen.LoadingScreen
import org.aahzbrut.darkmatter.system.AnimationSystem
import org.aahzbrut.darkmatter.system.AttachmentSystem
import org.aahzbrut.darkmatter.system.BoundingBoxRenderingSystem
import org.aahzbrut.darkmatter.system.EnemySystem
import org.aahzbrut.darkmatter.system.MoveSystem
import org.aahzbrut.darkmatter.system.PlayerAnimationSystem
import org.aahzbrut.darkmatter.system.PlayerInputSystem
import org.aahzbrut.darkmatter.system.PowerUpSystem
import org.aahzbrut.darkmatter.system.RemoveSystem
import org.aahzbrut.darkmatter.system.RenderSystem

private val LOG = logger<DarkMatter>()

class DarkMatter : KtxGame<BaseScreen>() {

    val gameViewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)

    val assetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }

    val audioService: AudioService by lazy { DefaultAudioService(assetStorage) }

    private val graphicsAtlas by lazy { assetStorage[TextureAtlasAsset.TEXTURE_ATLAS.descriptor] }

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
            addSystem(PlayerAnimationSystem(graphicsAtlas))
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
        addScreen(LoadingScreen(this))
        setScreen<LoadingScreen>()
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Disposing resources" }
        assetStorage.dispose()
        batch.dispose()
    }
}