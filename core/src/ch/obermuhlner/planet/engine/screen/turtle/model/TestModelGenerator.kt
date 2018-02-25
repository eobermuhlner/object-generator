package ch.obermuhlner.planet.engine.screen.turtle.model

import ch.obermuhlner.obj3d.turtle.Turtle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.math.Vector2

class TestModelGenerator : TurtleModelGenerator {
    override fun generate(turtle: Turtle, random: Random): Model {
        val texture = Texture(Gdx.files.internal("textures/test_diffuse.png").path())
        //val texture = Texture(Gdx.files.internal("textures/Brick1_diffuse.jpg").path())
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)

        val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
        val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
        val green = Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE))
        val test = Material(TextureAttribute.createDiffuse(texture))

        turtle.uvScale.set(-0.1f, 0.1f)

        turtle.regularPolygon(4, 5.0f, test)
        //turtle.polygon(test, Vector2(-1f, -1f), Vector2(1f, -1f), Vector2(1f, 1f), Vector2(-1f, 1f))
        turtle.smooth = true
        turtle.forward(0f)

        turtle.forward(5f)

        turtle.regularPolygon(3, 5.0f, test)
        turtle.forward(5f)

        turtle.forward(5f)

        turtle.regularPolygon(4, 5.0f, test)
        turtle.forward(5f)


        turtle.close()

        return turtle.end()
    }
}
