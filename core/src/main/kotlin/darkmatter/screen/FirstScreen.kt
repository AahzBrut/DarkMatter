package darkmatter.screen

import darkmatter.DarkMatter
import darkmatter.PLAYER_SIZE
import darkmatter.component.*
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger


private val LOG = logger<FirstScreen>()

class FirstScreen(game: DarkMatter) : BaseScreen(game) {

    private val player = engine.entity {
        with<TransformComponent> {
            position.set((gameViewport.worldWidth - PLAYER_SIZE) / 2, 1f, 0f)
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
        engine.update(delta)
    }

    override fun dispose() {
        LOG.debug { "Disposing resources" }
    }
}