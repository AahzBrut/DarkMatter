package org.aahzbrut.darkmatter.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.graphics.use
import org.aahzbrut.darkmatter.LIVES_INDICATOR_HEIGHT
import org.aahzbrut.darkmatter.LIVES_INDICATOR_WIDTH
import org.aahzbrut.darkmatter.MAX_PLAYER_LIVES
import org.aahzbrut.darkmatter.WORLD_HEIGHT_UI
import org.aahzbrut.darkmatter.asset.SpriteCache
import org.aahzbrut.darkmatter.event.GameEventListener
import org.aahzbrut.darkmatter.event.GameEventManagers
import org.aahzbrut.darkmatter.event.PlayerDamageEvent

class UIRenderSystem(
        private val batch: Batch,
        private val uiViewport: Viewport,
        spriteCache: SpriteCache
) : EntitySystem(), GameEventListener<PlayerDamageEvent> {

    private val playerLivesUI = spriteCache.getSprites("player/lives/LivesIndicator")

    private var numLives: Int = MAX_PLAYER_LIVES

    private val damageEventManager = GameEventManagers[PlayerDamageEvent::class]

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        damageEventManager.addEventListener(this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        damageEventManager.removeEventListener(this)
    }

    override fun onEvent(event: PlayerDamageEvent) {
        numLives = event.numLivesLeft
    }

    override fun update(deltaTime: Float) {
        uiViewport.apply()
        batch.use(uiViewport.camera.combined) {
            playerLivesUI[numLives].run {
                setBounds(10f, WORLD_HEIGHT_UI - LIVES_INDICATOR_HEIGHT - 10f, LIVES_INDICATOR_WIDTH, LIVES_INDICATOR_HEIGHT)
                setAlpha(.5f)
                draw(batch)
            }
        }
    }
}