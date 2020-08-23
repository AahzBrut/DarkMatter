package org.aahzbrut.darkmatter.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import org.aahzbrut.darkmatter.DarkMatter


object Lwjgl3Launcher {

    @JvmStatic
    fun main(args: Array<String>) {

        Lwjgl3Application(
                DarkMatter(),
                Lwjgl3ApplicationConfiguration().apply {
                    setTitle("DarkMatter")
                    setWindowedMode(1600, 900)
                    setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
                })
    }
}