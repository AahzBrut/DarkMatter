package org.aahzbrut.darkmatter.ui

import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.skin
import org.aahzbrut.darkmatter.asset.BitmapFontAsset.UI_FONT
import org.aahzbrut.darkmatter.asset.TextureAtlasAsset.UI_ATLAS

fun createSkin(assetStorage: AssetStorage) {
    val uiAtlas = assetStorage[UI_ATLAS.descriptor]
    val uiFont = assetStorage[UI_FONT.descriptor]

    Scene2DSkin.defaultSkin = skin(uiAtlas) {
        label("default") {
            font = uiFont
        }
    }
}