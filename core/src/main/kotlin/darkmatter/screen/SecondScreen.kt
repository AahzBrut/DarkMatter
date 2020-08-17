package darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import darkmatter.DarkMatter
import ktx.log.debug
import ktx.log.logger

private val LOG = logger<SecondScreen>()

class SecondScreen(game: DarkMatter) : BaseScreen(game) {

    override fun show() {
        LOG.debug { "Second screen is showing" }
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            game.setScreen<FirstScreen>()
    }
}