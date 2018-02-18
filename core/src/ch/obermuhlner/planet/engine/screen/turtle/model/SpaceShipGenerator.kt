package ch.obermuhlner.planet.engine.screen.turtle.model

import ch.obermuhlner.planet.engine.construction.turtle.Turtle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute

class SpaceShipGenerator : TurtleModelGenerator {
    private val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
    private val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
    private val green = Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE))

    override fun generate(body: Turtle, random: Random): Model {
        val cornerCount = 7
        val topSide = 0
        val leftWingSide = 2
        val rightWingSide = cornerCount - 2
        val leftFinSide = 1
        val rightFinSide = cornerCount - 1

        val bodyRadius = 5f

        body.regularPolygon(cornerCount, 0.00001f, white)
        body.forward(0f)

        body.radius = bodyRadius * 0.6f
        body.forward(5f)

        body.radius = bodyRadius
        val cockpit = body.sides[topSide].turtle()
        body.forward(5f)
        generateCockpit(cockpit, random)

        val leftWing = body.sides[leftWingSide].turtle()
        val rightWing = body.sides[rightWingSide].turtle()
        body.forward(25f)
        generateWing(leftWing, random)
        generateWing(rightWing, random)

        val tailFin = body.sides[topSide].turtle()
        val leftFin = body.sides[leftFinSide].turtle()
        val rightFin = body.sides[rightFinSide].turtle()
        body.radius = bodyRadius * 0.8f
        body.forward(15f)
        generateFin(tailFin, random)
        generateFin(leftFin, random)
        generateFin(rightFin, random)

        body.radius = bodyRadius * 0.5f
        body.forward(5f)

        body.close()

        return body.end()
    }

    private fun generateCockpit(cockpit: Turtle, random: Random) {
        cockpit.material = green
        cockpit.smooth = true
        cockpit.forward(0f)

        cockpit.radius *= 0.8f
        cockpit.forward(0.3f)

        cockpit.radius *= 0.5f
        cockpit.forward(0.1f)

        cockpit.close()
    }

    private fun generateWing(wing: Turtle, random: Random) {
        wing.forward(0f)
        wing.radius {_, old -> old * 0.6f}
        wing.forward(0f)

        wing.radius {_, old -> old * 0.4f}
        wing.forward(15f)

        wing.radius {_, old -> old * 0.2f}
        wing.forward(3f)

        wing.close()
    }

    private fun generateFin(fin: Turtle, random: Random) {
        fin.forward(0f)
        fin.radius { _, old -> old * 0.6f}
        fin.forward(0f)

        fin.radius { _, old -> old * 0.4f}
        fin.forward(5f)

        fin.radius { _, old -> old * 0.2f}
        fin.forward(1f)

        fin.close()
    }
}
