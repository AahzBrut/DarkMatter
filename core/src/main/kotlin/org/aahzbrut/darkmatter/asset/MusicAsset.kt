package org.aahzbrut.darkmatter.asset

enum class MusicAsset(private val asset: BaseMusicAsset) {
    BACKGROUND_MUSIC(BaseMusicAsset(fileName = "music_background.wav"));

    val descriptor
        get() = asset.descriptor
}
