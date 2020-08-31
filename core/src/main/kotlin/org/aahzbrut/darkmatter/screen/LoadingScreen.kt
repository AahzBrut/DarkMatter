package org.aahzbrut.darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.actors.plus
import ktx.actors.plusAssign
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import ktx.scene2d.*
import org.aahzbrut.darkmatter.DarkMatter
import org.aahzbrut.darkmatter.asset.BitmapFontAsset
import org.aahzbrut.darkmatter.asset.SoundAsset
import org.aahzbrut.darkmatter.asset.TextureAtlasAsset

private val LOG = logger<LoadingScreen>()

class LoadingScreen(game: DarkMatter) : BaseScreen(game) {

    private lateinit var progressBar: Image
    private lateinit var touchToBegin: Label


    override fun show() {
        LOG.debug { "Loading screen is showing" }
        val assetLoadStart = System.currentTimeMillis()

        val assetRefs = gdxArrayOf(
                TextureAtlasAsset.values().map { assetStorage.loadAsync(it.descriptor) },
                SoundAsset.values().map { assetStorage.loadAsync(it.descriptor) },
                BitmapFontAsset.values().map { assetStorage.loadAsync(it.descriptor) }
        ).flatten()

        KtxAsync.launch {
            assetRefs.joinAll()
            LOG.debug { "Assets loaded in ${System.currentTimeMillis() - assetLoadStart}ms" }
            onAssetsLoaded()
        }

        // Setup UI, display loading progress
        setUpUI()
    }

    private fun setUpUI() {
        stage.actors {
            table {
                defaults().fillX().expandX()

                label("Loading...", "default") {
                    wrap = true
                    setAlignment(Align.center)
                    setFontScale(.2f)
                }

                row()

                touchToBegin = label("Press any key to continue...", "default") {
                    wrap = true
                    setAlignment(Align.center)
                    setFontScale(.1f)
                    color = Color.GOLD
                    color.a = 0f
                }

                row()

                stack { cell ->
                    cell.padTop(5f).padBottom(5f)
                    cell.maxHeight(10f)
                    progressBar = image("LoadingBar").apply {
                        scaleX = 0f
                    }
                    cell.padLeft(5f).padRight(5f)
                }

                setFillParent(true)
                pack()
            }
        }
    }

    override fun render(delta: Float) {
        if (assetStorage.progress.isFinished && (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) && game.containsScreen<GameScreen>()) {
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }

        progressBar.scaleX = assetStorage.progress.percent

        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

    override fun hide() {
        stage.clear()
    }

    private fun onAssetsLoaded() {
        game.addScreen(GameScreen(game))
        progressBar.isVisible = false
        touchToBegin += forever(sequence(fadeIn(.5f) + fadeOut(.5f)))
    }
}