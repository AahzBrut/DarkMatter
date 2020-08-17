package darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.viewport.FitViewport
import darkmatter.DarkMatter
import darkmatter.WORLD_HEIGHT
import darkmatter.WORLD_WIDTH
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger


private val LOG = logger<FirstScreen>()

class FirstScreen(game: DarkMatter) : BaseScreen(game) {

    private val viewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)
    private val texture = Texture(Gdx.files.internal("sprites/player.png"))
    private val sprite = Sprite(texture).apply {
        setSize(4f,4f)
    }

    override fun show() {
        LOG.debug { "First screen is showing" }
        sprite.setPosition((viewport.worldWidth - sprite.width) / 2, 1f)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply()
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            game.setScreen<SecondScreen>()

        batch.use(viewport.camera.combined) {
            sprite.draw(it)
        }
    }

    override fun dispose() {
        LOG.debug { "Disposing resources" }
        texture.dispose()
    }
}