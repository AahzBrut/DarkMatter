package org.aahzbrut.darkmatter.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration.getDisplayMode
import org.aahzbrut.darkmatter.DarkMatter


object Lwjgl3Launcher {

    @JvmStatic
    fun main(args: Array<String>) {

        Lwjgl3Application(
                DarkMatter(),
                Lwjgl3ApplicationConfiguration().apply {
                    setTitle("DarkMatter")
                    setFullscreenMode(getDisplayMode())
                    useVsync(false)
                    setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
                })
    }
}