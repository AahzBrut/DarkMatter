package org.aahzbrut.darkmatter.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Disposable
import org.aahzbrut.darkmatter.asset.TextureAtlasAsset.*


open class BaseAsset<T : Disposable>(
        directory: String,
        fileName: String,
        asset: Class<T>) {

    open val descriptor: AssetDescriptor<T> = AssetDescriptor("$directory/$fileName", asset)
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

class BaseBitmapFontAsset(
        directory: String = "fonts",
        fileName: String,
        asset: Class<BitmapFont> = BitmapFont::class.java
) : BaseAsset<BitmapFont>(directory, fileName = fileName, asset = asset) {

    override val descriptor: AssetDescriptor<BitmapFont> = AssetDescriptor(
            "$directory/$fileName",
            asset,
            BitmapFontLoader.BitmapFontParameter().apply {
                atlasName = TEXTURE_ATLAS.descriptor.fileName
            }
    )
}