package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.assets.async.AssetStorage
import ktx.graphics.use
import org.aahzbrut.darkmatter.*
import org.aahzbrut.darkmatter.asset.BitmapFontAsset.SCORE_FONT
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.event.*

class UIRenderSystem(
        private val batch: Batch,
        private val uiViewport: Viewport,
        spriteCache: SpriteCache,
        assetStorage: AssetStorage
) : EntitySystem(),
        GameEventListener<ScoreEvent> {

    private val playerLivesUI = spriteCache.getSprites("player/lives/LivesIndicator")

    private val scoreEventManager = GameEventManagers[ScoreEvent::class]

    private val font: BitmapFont = assetStorage[SCORE_FONT.descriptor]

    init {
        font.color.a = .4f

        playerLivesUI.forEach {
            it.setBounds(10f, WORLD_HEIGHT_UI - LIVES_INDICATOR_HEIGHT - 10f, LIVES_INDICATOR_WIDTH, LIVES_INDICATOR_HEIGHT)
            it.setAlpha(.5f)
        }
    }

    private var numLives: Int = MAX_PLAYER_LIVES

    private var score: Int = 0

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        scoreEventManager.addEventListener(this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        scoreEventManager.removeEventListener(this)
    }

    override fun onEvent(event: ScoreEvent) {
        score = event.score
        numLives = event.numLivesLeft
    }

    override fun update(deltaTime: Float) {
        uiViewport.apply()
        batch.use(uiViewport.camera.combined) {
            playerLivesUI[numLives].run {
                draw(batch)
            }

            font.draw(batch, "%06d".format(score), WORLD_WIDTH_UI - 110f, WORLD_HEIGHT_UI - 10f)
        }
    }
}