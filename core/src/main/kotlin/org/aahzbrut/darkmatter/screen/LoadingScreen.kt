package org.aahzbrut.darkmatter.screen

import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.DarkMatter
import org.aahzbrut.darkmatter.asset.TextureAtlasAsset

private val LOG = logger<LoadingScreen>()

class LoadingScreen(game: DarkMatter) : BaseScreen(game) {

    override fun show() {
        LOG.debug { "Loading screen is showing" }
        val assetLoadStart = System.currentTimeMillis()

        val assetRefs = gdxArrayOf(
                TextureAtlasAsset.values().map {
                    assetStorage.loadAsync(it.descriptior)
                }
        ).flatten()

        KtxAsync.launch {
            assetRefs.joinAll()
            LOG.debug { "Assets loaded in ${System.currentTimeMillis() - assetLoadStart}ms" }
            onAssetsLoaded()
        }

        // Setup UI, display loading progress
    }

    private fun onAssetsLoaded() {
        game.addScreen(GameScreen(game))
        game.setScreen<GameScreen>()
        game.removeScreen<LoadingScreen>()
        dispose()
    }
}