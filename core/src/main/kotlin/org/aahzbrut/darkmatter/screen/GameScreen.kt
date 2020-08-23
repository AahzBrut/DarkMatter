package org.aahzbrut.darkmatter.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.FPSLogger
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import org.aahzbrut.darkmatter.DarkMatter
import org.aahzbrut.darkmatter.MAX_DELTA_TIME
import org.aahzbrut.darkmatter.PLAYER_BOUNDING_BOX
import org.aahzbrut.darkmatter.PLAYER_SIZE
import org.aahzbrut.darkmatter.WORLD_HEIGHT
import org.aahzbrut.darkmatter.WORLD_WIDTH
import org.aahzbrut.darkmatter.asset.TextureAtlasAsset
import org.aahzbrut.darkmatter.component.AnimationComponent
import org.aahzbrut.darkmatter.component.AnimationType
import org.aahzbrut.darkmatter.component.AttachmentComponent
import org.aahzbrut.darkmatter.component.BoundingBoxComponent
import org.aahzbrut.darkmatter.component.GraphicComponent
import org.aahzbrut.darkmatter.component.MoveComponent
import org.aahzbrut.darkmatter.component.PlayerComponent
import org.aahzbrut.darkmatter.component.RollComponent
import org.aahzbrut.darkmatter.component.TransformComponent
import kotlin.math.min


private val LOG = logger<GameScreen>()
private var fpsLogger = FPSLogger()

class GameScreen(
        game: DarkMatter,
        val engine: Engine = game.engine) :
        BaseScreen(game) {

    private val graphicsAtlas = game.assetStorage[TextureAtlasAsset.TEXTURE_ATLAS.descriptior]

    override fun show() {
        LOG.debug { "First screen is showing" }

        engine.entity {
            with<TransformComponent>{
                setInitialPosition(0f,0f,-1f)
                size.set(WORLD_WIDTH, WORLD_HEIGHT)
            }
            with<GraphicComponent>{
                setSpriteRegion(graphicsAtlas.findRegion("background/SpaceBG_Overlay"))
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
        fpsLogger.log()
    }

    override fun dispose() {
        LOG.debug { "Disposing resources" }
    }
}