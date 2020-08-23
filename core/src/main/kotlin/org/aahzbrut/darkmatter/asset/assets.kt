package org.aahzbrut.darkmatter.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable


open class BaseAsset<T : Disposable>(
        directory: String,
        fileName: String,
        asset: Class<T>) {

    val descriptor: AssetDescriptor<T> = AssetDescriptor("$directory/$fileName", asset)
}

class BaseAtlasAsset(
        directory: String = "graphics",
        fileName: String,
        asset: Class<TextureAtlas> = TextureAtlas::class.java
) : BaseAsset<TextureAtlas>(directory, fileName = fileName, asset = asset)

class BaseSoundAsset(
        directory: String = "sounds",
        fileName: String,
        asset: Class<Sound> = Sound::class.java
) : BaseAsset<Sound>(directory, fileName = fileName, asset = asset)

class BaseMusicAsset(
        directory: String = "music",
        fileName: String,
        asset: Class<Music> = Music::class.java
) : BaseAsset<Music>(directory, fileName = fileName, asset = asset)
