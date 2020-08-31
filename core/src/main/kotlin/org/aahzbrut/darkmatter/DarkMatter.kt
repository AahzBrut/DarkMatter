package org.aahzbrut.darkmatter

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.async.newAsyncContext
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.asset.BitmapFontAsset
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.asset.TextureAtlasAsset
import org.aahzbrut.darkmatter.audio.AudioService
import org.aahzbrut.darkmatter.audio.DefaultAudioService
import org.aahzbrut.darkmatter.screen.BaseScreen
import org.aahzbrut.darkmatter.screen.LoadingScreen
import org.aahzbrut.darkmatter.system.*
import org.aahzbrut.darkmatter.ui.createSkin

private val LOG = logger<DarkMatter>()

class DarkMatter : KtxGame<BaseScreen>() {

    val gameViewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)

    val uiViewport = FitViewport(WORLD_WIDTH_UI, WORLD_HEIGHT_UI)

    val stage by lazy {
        val result = Stage(uiViewport, batch)
        Gdx.input.inputProcessor = result
        result
    }

    val assetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage(newAsyncContext(ASSET_STORAGE_LOADER_THREAD_NUMBER, "AssetStorage-Thread"))
    }

    val audioService: AudioService by lazy { DefaultAudioService(assetStorage) }

    val spriteCache by lazy {
        SpriteCache(graphicsAtlas)
    }

    private val graphicsAtlas by lazy { assetStorage[TextureAtlasAsset.TEXTURE_ATLAS.descriptor] }

    private val batch: Batch by lazy { SpriteBatch() }

    private val shapeRenderer by lazy {
        val shapeRenderer = ShapeRenderer()
        shapeRenderer.projectionMatrix = gameViewport.camera.combined
        shapeRenderer
    }

    val engine by lazy {
        PooledEngine(1000, 10000, 5000, 50000).apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(PowerUpSystem(audioService))
            addSystem(EnemySystem(spriteCache, audioService))
            addSystem(PlayerAnimationSystem(spriteCache))
            addSystem(AttachmentSystem())
            addSystem(AnimationSystem(spriteCache))
            addSystem(RenderSystem(batch, gameViewport))
            addSystem(BoundingBoxRenderingSystem(gameViewport, shapeRenderer))
            addSystem(UIRenderSystem(batch, uiViewport, spriteCache, assetStorage))
            addSystem(WeaponSystem(spriteCache, audioService))
            addSystem(ProjectileSystem())
            addSystem(PowerUpEffectSystem())
            addSystem(RemoveSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        LOG.debug { "IN: DarkMatter::create()" }

        val assetLoaders = gdxArrayOf(
                TextureAtlasAsset.values().filter { it.name == "UI_ATLAS" }.map { assetStorage.loadAsync(it.descriptor) },
                BitmapFontAsset.values().filter { it.name == "UI_FONT" }.map { assetStorage.loadAsync(it.descriptor) }
        ).flatten()

        KtxAsync.launch {
            assetLoaders.joinAll()
            createSkin(assetStorage)
            addScreen(LoadingScreen(this@DarkMatter))
            setScreen<LoadingScreen>()
        }
    }

    override fun dispose() {
        super.dispose()
        LOG.debug { "Disposing resources" }
        assetStorage.dispose()
        batch.dispose()
        shapeRenderer.dispose()
        graphicsAtlas.dispose()
        stage.dispose()
    }
}