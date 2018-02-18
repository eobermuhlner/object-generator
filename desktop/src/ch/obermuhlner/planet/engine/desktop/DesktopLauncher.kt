package ch.obermuhlner.planet.engine.desktop

import ch.obermuhlner.planet.engine.PlanetEngine
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.samples = 4
    LwjglApplication(PlanetEngine(), config)
}