package ch.obermuhlner.planet.engine.screen.turtle.model

import ch.obermuhlner.obj3d.turtle.Turtle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute

class SimpleSpaceShipGenerator : TurtleModelGenerator {
    private val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
    private val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
    private val black = Material(ColorAttribute.createDiffuse(Color.BLACK), ColorAttribute.createSpecular(Color.WHITE))

    override fun generate(body: Turtle, random: Random): Model {
        val cornerCount = 7
        val topSide = 0
        val leftWingSide = 2
        val rightWingSide = cornerCount - 2

        body.uvScale.set(0.1f, 0.1f)

        body.moveForward(-30f)

        body.regularPolygon(cornerCount, 0.000001f, white)
        body.smooth = false
        body.forward(0f)

        body.radius = 0.2f
        body.forward(5f)

        body.radius = 2f
        body.forward(10f)

        val cockpit = body.sides[topSide].turtle()
        body.radius = 5f
        body.forward(20f)
        generateCockpit(cockpit)

        body.material = white
        val leftWing = body.sides[leftWingSide].turtle()
        val rightWing = body.sides[rightWingSide].turtle()
        body.forward(30f)
        generateWing(leftWing, 0.2f, 40f, 30f, 0.8f, 0.4f, -40f, -20f, 5f)
        generateWing(rightWing, 0.2f, 40f, 30f, 0.8f, 0.4f, -40f, -20f, -5f)

        val leftFin = body.sides[leftWingSide].turtle()
        val rightFin = body.sides[rightWingSide].turtle()
        val topFin = body.sides[topSide].turtle()
        body.radius = 4f
        body.forward(15f)
        generateWing(leftFin, 0.1f, 15f, 10f, 0.5f, 0.2f, -30f, 0f, -60f)
        generateWing(rightFin, 0.1f, 15f, 10f, 0.5f, 0.2f, -30f, 0f, 60f)
        generateWing(topFin, 0.1f, 10f, 5f, 0.5f, 0.2f, -30f, -10f, 0f)

        body.radius = 3f
        body.forward(5f)

        body.radius {_, old -> old * 0.9f }
        body.forward(-4f)

        body.material = red
        body.close()

        return body.end()
    }

    private fun generateWing(wing: Turtle?, connectorLength: Float, mainLength: Float, tipLength: Float, startWidthFactor: Float, midWidthFactor: Float, firstAngle: Float, secondAngle: Float, upAngle: Float) {
        if (wing == null) {
            return
        }

        wing.radius {_, old -> old * startWidthFactor }
        wing.forward(connectorLength)

        wing.forwardDirection.rotate(wing.upDirection, firstAngle)
        wing.radius {_, old -> old * midWidthFactor }
        wing.forward(mainLength)

        wing.forwardDirection.rotate(wing.sideDirection, upAngle)
        wing.forwardDirection.rotate(wing.upDirection, secondAngle)
        wing.radius {_, old -> old * 0.2f}
        wing.forward(tipLength)

        wing.close()
    }

    private fun generateCockpit(cockpit: Turtle) {
        cockpit.material = black
        cockpit.smooth = true

        cockpit.radius {_, old -> old * 0.5f}
        cockpit.forward(0.5f)

        cockpit.close()
    }
}
