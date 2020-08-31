package org.aahzbrut.darkmatter.asset

import org.aahzbrut.darkmatter.asset.TextureAtlasAsset.*

enum class BitmapFontAsset(private val asset: BaseBitmapFontAsset) {
    SCORE_FONT(BaseBitmapFontAsset(fileName = "font.fnt", fontAtlasName = TEXTURE_ATLAS.descriptor.fileName)),
    UI_FONT(BaseBitmapFontAsset("ui", fileName = "GoodTiming.fnt", fontAtlasName = UI_ATLAS.descriptor.fileName));

    val descriptor
        get() = asset.descriptor
}
