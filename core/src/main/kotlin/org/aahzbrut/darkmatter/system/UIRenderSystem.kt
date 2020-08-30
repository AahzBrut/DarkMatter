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
import org.aahzbrut.darkmatter.event.GameEventListener
import org.aahzbrut.darkmatter.event.GameEventManagers
import org.aahzbrut.darkmatter.event.PlayerDamageEvent
import org.aahzbrut.darkmatter.event.ScoreEvent

class UIRenderSystem(
        private val batch: Batch,
        private val uiViewport: Viewport,
        spriteCache: SpriteCache,
        assetStorage: AssetStorage
) : EntitySystem() {

    private class ScoreEventListener(private val receiver: UIRenderSystem) : GameEventListener<ScoreEvent> {
        override fun onEvent(event: ScoreEvent) {
            receiver.score = event.score
        }
    }

    private class PlayerDamageEventListener(private val receiver: UIRenderSystem) : GameEventListener<PlayerDamageEvent> {
        override fun onEvent(event: PlayerDamageEvent) {
            receiver.numLives = event.numLivesLeft
        }
    }

    private val scoreEventListener = ScoreEventListener(this)

    private val playerDamageEventListener = PlayerDamageEventListener(this)

    private val playerLivesUI = spriteCache.getSprites("player/lives/LivesIndicator")

    private val scoreEventManager = GameEventManagers[ScoreEvent::class]

    private val playerDamageEventManager = GameEventManagers[PlayerDamageEvent::class]

    private val font: BitmapFont = assetStorage[SCORE_FONT.descriptor]

    init {
        font.color.a = .4f
        font.data.setScale(.8f)

        playerLivesUI.forEach {
            it.setBounds(10f, WORLD_HEIGHT_UI - LIVES_INDICATOR_HEIGHT - 10f, LIVES_INDICATOR_WIDTH, LIVES_INDICATOR_HEIGHT)
            it.setAlpha(.5f)
        }
    }

    private var numLives: Int = MAX_PLAYER_LIVES

    private var score: Int = 0

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        scoreEventManager.addEventListener(scoreEventListener)
        playerDamageEventManager.addEventListener(playerDamageEventListener)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        scoreEventManager.removeEventListener(scoreEventListener)
        playerDamageEventManager.removeEventListener(playerDamageEventListener)
    }

    override fun update(deltaTime: Float) {
        uiViewport.apply()
        batch.use(uiViewport.camera.combined) {
            playerLivesUI[numLives].run {
                draw(batch)
            }

            font.draw(batch, "%06d".format(score), WORLD_WIDTH_UI - 100f, WORLD_HEIGHT_UI - 10f)
        }
    }
}