package org.aahzbrut.darkmatter.asset

enum class SoundAsset(private val asset: BaseSoundAsset) {
    EXPLOSION(BaseSoundAsset(fileName = "explosion_sound.wav")),
    SHOT(BaseSoundAsset(fileName = "laser_shot.wav")),
    POWER_UP(BaseSoundAsset(fileName = "power_up_sound.wav"));

    val descriptor
        get() = asset.descriptor
}
