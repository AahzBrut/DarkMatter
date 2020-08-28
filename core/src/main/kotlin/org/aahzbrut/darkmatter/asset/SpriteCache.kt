package org.aahzbrut.darkmatter.asset

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.collections.GdxArray
import ktx.collections.GdxMap
import ktx.collections.set
import ktx.log.logger
import org.aahzbrut.darkmatter.component.EmptySprite

@Suppress("UNUSED")
private val LOG = logger<SpriteCache>()

object EmptySpriteArray : GdxArray<Sprite>()

class SpriteCache(textureAtlas: TextureAtlas) {

    private val cache: GdxMap<String, GdxArray<Sprite>> = GdxMap(textureAtlas.regions.size)

    init {
        textureAtlas.regions.map {
            it.name
        }
                .distinct()
                .forEach {
                    cache[it] = textureAtlas.createSprites(it)
                }
    }

    fun getSprite(name: String) = cache[name]?.get(0) ?: EmptySprite

    fun getSprites(name: String) = cache[name] ?: EmptySpriteArray
}