package darkmatter.screen

import darkmatter.DarkMatter
import darkmatter.MAX_DELTA_TIME
import darkmatter.PLAYER_SIZE
import darkmatter.component.*
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import kotlin.math.min


private val LOG = logger<FirstScreen>()

class FirstScreen(game: DarkMatter) : BaseScreen(game) {

    private val player = engine.entity {
        with<TransformComponent> {
            setInitialPosition((gameViewport.worldWidth - PLAYER_SIZE) / 2, 1f, 0f)
            size.set(PLAYER_SIZE, PLAYER_SIZE)
        }
        with<MoveComponent>{}
        with<GraphicComponent>{}
        with<PlayerComponent>{}
        with<RollComponent>{}
    }

    override fun show() {
        LOG.debug { "First screen is showing" }
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
    }

    override fun dispose() {
        LOG.debug { "Disposing resources" }
    }
}