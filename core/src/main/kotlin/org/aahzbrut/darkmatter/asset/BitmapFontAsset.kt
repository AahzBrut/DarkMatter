package org.aahzbrut.darkmatter.asset

enum class BitmapFontAsset(private val asset: BaseBitmapFontAsset) {
    SCORE_FONT(BaseBitmapFontAsset(fileName = "font.fnt"));

    val descriptor
        get() = asset.descriptor
}
