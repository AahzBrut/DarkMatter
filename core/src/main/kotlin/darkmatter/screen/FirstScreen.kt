package darkmatter.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import darkmatter.DarkMatter
import darkmatter.PLAYER_SIZE
import darkmatter.WORLD_HEIGHT
import darkmatter.WORLD_WIDTH
import darkmatter.component.GraphicComponent
import darkmatter.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger


private val LOG = logger<FirstScreen>()

class FirstScreen(game: DarkMatter) : BaseScreen(game) {

    private val viewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT)
    private val playerTexture = Texture(Gdx.files.internal("sprites/player.png"))
    private val player = engine.entity {
        with<TransformComponent> {
            position.set((viewport.worldWidth - PLAYER_SIZE) / 2, 1f, 0f)
            size.set(PLAYER_SIZE, PLAYER_SIZE)
        }
        with<GraphicComponent> {
            sprite.run {
                setRegion(playerTexture)
                setOriginCenter()
            }
        }
    }

    override fun show() {
        LOG.debug { "First screen is showing" }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        engine.update(delta)
        viewport.apply()
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            game.setScreen<SecondScreen>()

        batch.use(viewport.camera.combined) {
            player[GraphicComponent.mapper]?.let {graphic ->
                player[TransformComponent.mapper]?.let {transform ->
                    graphic.sprite.run {
                        rotation = transform.rotation
                        setBounds(transform.position.x, transform.position.y, transform.size.x, transform.size.y)
                        draw(batch)
                    }
                }
            }
        }
    }

    override fun dispose() {
        LOG.debug { "Disposing resources" }
        playerTexture.dispose()
    }
}