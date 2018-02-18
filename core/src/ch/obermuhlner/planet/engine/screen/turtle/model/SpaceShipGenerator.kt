package ch.obermuhlner.planet.engine.screen.turtle.model

import ch.obermuhlner.planet.engine.construction.turtle.SideType
import ch.obermuhlner.planet.engine.construction.turtle.Turtle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute

class SpaceShipGenerator : TurtleModelGenerator {
    override fun generate(turtle: Turtle, random: Random): Model {
        val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
        val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
        val green = Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE))

        val body = Turtle()

        body.regularPolygon(4, 0.00001f, white)
        body.forward(0f)

        body.radius = 4f
        body.forward(5f)

        body.sides[1].type = SideType.Turtle
        val cockpit = body.forward(10f)[0]

        cockpit.material = green
        cockpit.smooth = true
        cockpit.forward(0f)
        cockpit.radius *= 0.8f
        cockpit.forward(1f)
        cockpit.radius *= 0.5f
        cockpit.forward(1f)
        cockpit.close()

        body.sides[1].type = SideType.Face

        body.forward(10f)

        body.close()

        return body.end()
    }
}
