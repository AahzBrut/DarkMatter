package org.aahzbrut.darkmatter.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Pool
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import org.aahzbrut.darkmatter.MAX_SOUND_INSTANCES
import org.aahzbrut.darkmatter.asset.MusicAsset
import org.aahzbrut.darkmatter.asset.SoundAsset
import java.util.*
import kotlin.math.max

private val LOG = logger<AudioService>()

interface AudioService {
    fun play(soundAsset: SoundAsset, volume: Float = 1f, pitch: Float = 1f, pan: Float = 0f)
    fun play(musicAsset: MusicAsset, volume: Float = 1f, loop: Boolean = true)
    fun pause()
    fun resume()
    fun stop(clearSounds: Boolean = true)
    fun update()
}

private class SoundRequest : Pool.Poolable {
    lateinit var soundAsset: SoundAsset
    var volume = 1f
    var pitch = 1f
    var pan = 0f

    override fun reset() {
        volume = 1f
        pitch = 1f
        pan = 0f
    }
}

private class SoundRequestPool : Pool<SoundRequest>() {
    override fun newObject() = SoundRequest()
}

class DefaultAudioService(private val assetStorage: AssetStorage) : AudioService {

    private val soundCache = EnumMap<SoundAsset, Sound>(SoundAsset::class.java)
    private val soundRequestPool = SoundRequestPool()
    private val soundRequests = EnumMap<SoundAsset, SoundRequest>(SoundAsset::class.java)
    private var currentMusic: Music? = null
    private var currentMusicAsset: MusicAsset? = null

    override fun play(soundAsset: SoundAsset, volume: Float, pitch: Float, pan: Float) {
        when {
            soundAsset in soundRequests -> {
                // same request multiple times in one frame -> set volume to maximum of both requests
                //LOG.debug { "Duplicated sound request for sound $soundAsset" }
                soundRequests[soundAsset]?.let { request ->
                    request.volume = max(request.volume, volume)
                    request.pitch = pitch
                    request.pan = pan
                }
            }
            soundRequests.size >= MAX_SOUND_INSTANCES -> {
                // maximum simultaneous sound instances reached -> do nothing
                LOG.debug { "Maximum sound instances reached" }
            }
            else -> {
                // new request
                if (soundAsset.descriptor !in assetStorage) {
                    // sound not loaded -> error
                    LOG.error { "Sound $soundAsset is not loaded" }
                    return
                } else if (soundAsset !in soundCache) {
                    // cache sound for faster access in the future
                    //LOG.debug { "Adding sound $soundAsset to sound cache" }
                    soundCache[soundAsset] = assetStorage[soundAsset.descriptor]
                }

                // get request instance from pool and add it to the queue
                soundRequests[soundAsset] = soundRequestPool.obtain().apply {
                    this.soundAsset = soundAsset
                    this.volume = volume
                    this.pitch = pitch
                    this.pan = pan
                }
            }
        }
    }

    override fun play(musicAsset: MusicAsset, volume: Float, loop: Boolean) {

        val musicDeferred = assetStorage.loadAsync(musicAsset.descriptor)
        KtxAsync.launch {
            musicDeferred.join()
            if (assetStorage.isLoaded(musicAsset.descriptor)) {

                // stop and unload current music before switch to new
                currentMusic?.stop()
                currentMusicAsset?.let { assetStorage.unload(it.descriptor) }

                currentMusicAsset = musicAsset
                currentMusic = assetStorage[musicAsset.descriptor].apply {
                    this.volume = volume
                    this.isLooping = loop
                    play()
                }
            }
        }
    }

    override fun pause() {
        currentMusic?.pause()
    }

    override fun resume() {
        currentMusic?.play()
    }

    override fun stop(clearSounds: Boolean) {
        currentMusic?.stop()
        if (clearSounds) {
            soundRequests.clear()
        }
    }

    override fun update() {
        if (!soundRequests.isEmpty()) {
            // there are sounds to be played
            //LOG.debug { "Playing ${soundRequests.size} sound(s)" }
            soundRequests.values.forEach { request ->
                soundCache[request.soundAsset]?.play(request.volume, request.pitch, request.pan)
                soundRequestPool.free(request)
            }
            soundRequests.clear()
        }
    }
}