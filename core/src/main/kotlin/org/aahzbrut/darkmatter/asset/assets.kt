package org.aahzbrut.darkmatter.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas

enum class TextureAtlasAsset(
        fileName: String,
        directory: String = "graphics",
        val descriptior: AssetDescriptor<TextureAtlas> = AssetDescriptor("$directory/$fileName", TextureAtlas::class.java)) {
    TEXTURE_ATLAS("textures.atlas")
}
