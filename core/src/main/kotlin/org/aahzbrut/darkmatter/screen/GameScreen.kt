package org.aahzbrut.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.FPSLogger
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.*
import org.aahzbrut.darkmatter.asset.MusicAsset
import org.aahzbrut.darkmatter.component.*
import kotlin.math.min


private val LOG = logger<GameScreen>()
private var fpsLogger = FPSLogger()

class GameScreen(
        game: DarkMatter,
        val engine: Engine = game.engine) :
        BaseScreen(game) {

    override fun show() {
        LOG.debug { "First screen is showing" }

        game.audioService.play(MusicAsset.BACKGROUND_MUSIC, 1f)

        engine.entity {
            with<TransformComponent> {
                setInitialPosition(0f, 0f, -1f)
                size.set(WORLD_WIDTH, WORLD_HEIGHT)
            }
            with<GraphicComponent> {
                resetSprite(game.spriteCache.getSprite("background/SpaceBG_Overlay"))
            }
        }

        val player = engine.entity {
            with<TransformComponent> {
                setInitialPosition((gameViewport.worldWidth - PLAYER_SIZE) / 2, 1f, 0f)
                size.set(PLAYER_SIZE, PLAYER_SIZE)
            }
            with<BoundingBoxComponent> {
                boundingBox.set(PLAYER_BOUNDING_BOX)
            }
            with<MoveComponent> {}
            with<GraphicComponent> {}
            with<PlayerComponent> {}
            with<RollComponent> {}
            with<WeaponComponent> {
                mainGunPosition.set(1.75f, 3.5f)
                leftGunPosition.set(.5f, 1f)
                rightGunPosition.set(PLAYER_SIZE - 1f, 1f)
            }
        }

        engine.entity {
            with<TransformComponent> { size.set(PLAYER_SIZE, PLAYER_SIZE) }
            with<AnimationComponent> {
                type = AnimationType.THRUSTER
            }
            with<GraphicComponent> {}
            with<AttachmentComponent> {
                entity = player
                offset.set(0f, -3f)
            }
        }
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
        game.audioService.update()
        // fpsLogger.log()
    }

    override fun dispose() {
        LOG.debug { "Disposing resources" }
    }
}