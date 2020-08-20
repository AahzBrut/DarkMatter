package darkmatter.screen

import com.badlogic.gdx.graphics.FPSLogger
import darkmatter.DarkMatter
import darkmatter.MAX_DELTA_TIME
import darkmatter.PLAYER_SIZE
import darkmatter.component.AnimationComponent
import darkmatter.component.AnimationType
import darkmatter.component.AttachmentComponent
import darkmatter.component.GraphicComponent
import darkmatter.component.MoveComponent
import darkmatter.component.PlayerComponent
import darkmatter.component.RollComponent
import darkmatter.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import kotlin.math.min


private val LOG = logger<FirstScreen>()
private var fpsLogger = FPSLogger()

class FirstScreen(game: DarkMatter) : BaseScreen(game) {

    override fun show() {
        LOG.debug { "First screen is showing" }

        val player = engine.entity {
            with<TransformComponent> {
                setInitialPosition((gameViewport.worldWidth - PLAYER_SIZE) / 2, 1f, 0f)
                size.set(PLAYER_SIZE, PLAYER_SIZE)
            }
            with<MoveComponent>{}
            with<GraphicComponent>{}
            with<PlayerComponent>{}
            with<RollComponent>{}
        }

        engine.entity {
            with<TransformComponent>{size.set(PLAYER_SIZE, PLAYER_SIZE)}
            with<AnimationComponent>{
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