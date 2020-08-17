package lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import darkmatter.DarkMatter

fun main() {

    Lwjgl3Application(
            DarkMatter(),
            Lwjgl3ApplicationConfiguration().apply {
                setTitle("DarkMatter")
                setWindowedMode(640, 480)
                setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
            })
}

