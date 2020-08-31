package org.aahzbrut.darkmatter.asset

enum class TextureAtlasAsset(private val asset: BaseAtlasAsset) {
    TEXTURE_ATLAS(BaseAtlasAsset(fileName = "textures.atlas")),
    UI_ATLAS(BaseAtlasAsset("ui", "ui.atlas"));

    val descriptor
        get() = asset.descriptor
}
