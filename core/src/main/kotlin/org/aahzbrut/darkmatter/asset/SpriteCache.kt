package org.aahzbrut.darkmatter.asset

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.collections.GdxArray
import ktx.log.logger

private val LOG = logger<SpriteCache>()

class SpriteCache(private val textureAtlas: TextureAtlas) {

    private val cache = textureAtlas.regions.associateBy(
            { it.name },
            { GdxArray<Sprite>(textureAtlas.createSprites(it.name)) }
    )

    fun getSprite(name: String, index: Int) = cache[name]?.get(index)?:Sprite()

    fun getSprite(name: String) = cache[name]?.get(0)?:Sprite()

    fun getSprites(name: String) = cache[name]?:GdxArray()
}