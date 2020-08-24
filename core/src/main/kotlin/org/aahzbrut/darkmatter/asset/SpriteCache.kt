package org.aahzbrut.darkmatter.asset

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.collections.GdxArray
import ktx.log.logger
import org.aahzbrut.darkmatter.component.EmptySprite

private val LOG = logger<SpriteCache>()

object EmptySpriteArray : GdxArray<Sprite>()

class SpriteCache(private val textureAtlas: TextureAtlas) {

    private val cache = textureAtlas.regions.associateBy(
            { it.name },
            { GdxArray<Sprite>(textureAtlas.createSprites(it.name)) }
    )

    fun getSprite(name: String, index: Int) = cache[name]?.get(index) ?: EmptySprite

    fun getSprite(name: String) = cache[name]?.get(0) ?: EmptySprite

    fun getSprites(name: String) = cache[name] ?: EmptySpriteArray
}