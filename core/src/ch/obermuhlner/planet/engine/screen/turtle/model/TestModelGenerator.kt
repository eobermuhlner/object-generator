package ch.obermuhlner.planet.engine.screen.turtle.model

import ch.obermuhlner.planet.engine.construction.turtle.Turtle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute

class TestModelGenerator : TurtleModelGenerator {
    override fun generate(turtle: Turtle, random: Random): Model {
        val texture = Texture(Gdx.files.internal("textures/test_diffuse.png").path())

        val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
        val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
        val green = Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE))
        val test = Material(TextureAttribute.createDiffuse(texture))

        turtle.regularPolygon(4, 4.0f, white)
        turtle.smooth = false
        turtle.forward(0f)

        turtle.forward(2f)

        turtle.radius = 5f
        val subTurtle = turtle.sides[1].turtle()
        turtle.forward(10.0f)

        subTurtle.forward(3f)
        subTurtle.radius = 0f
        subTurtle.forward(3f)

        turtle.rotate(10f)
        turtle.radius = 3f
        turtle.sides[0].material = green
        turtle.forward(10.0f)

        turtle.radius = 1f
        turtle.forward(0.0f)

        turtle.radius = 0.5f
        turtle.material = red
        turtle.forward(5.0f)

        turtle.close()

        return turtle.end()
    }
}
