package ch.obermuhlner.planet.engine

import ch.obermuhlner.planet.engine.screen.building.BuildingScreen
import ch.obermuhlner.planet.engine.screen.turtle.TurtleScreen
import ch.obermuhlner.planet.engine.screen.welcome.WelcomeScreen
import com.badlogic.gdx.Screen
import ktx.app.KtxGame

/**
 * Created by Eric on 1/7/2018.
 */
class PlanetEngine : KtxGame<Screen>() {
    override fun create() {
        addScreen(WelcomeScreen())
        addScreen(BuildingScreen())
        addScreen(TurtleScreen())

        //setScreen<BuildingScreen>()
        setScreen<TurtleScreen>()
    }
}
