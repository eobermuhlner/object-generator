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
        val cornerCount = random.nextInt(2, 5) * 2 + 1
        val topSide = 0
        val leftWingSide = 2
        val rightWingSide = cornerCount - 2
        val finsOnSameSideAsWings = random.nextBoolean(0.5f)
        val leftFinSide = if (finsOnSameSideAsWings) leftWingSide else 1
        val rightFinSide = if (finsOnSameSideAsWings) rightWingSide else cornerCount - 1

        val wingLength = random.nextFloat(20f, 30f)
        val wingAngle = random.nextFloat(-5f, -50f)

        val sideFinLength = random.nextFloat(10f, 20f)
        val sideFinAngle = random.nextFloat(-5f, -50f)

        val topFinLength = random.nextFloat(10f, 20f)
        val topFinAngle = random.nextFloat(-5f, -50f)

        val bodyRadius = random.nextFloat(3f, 6f)
        val bodyLength = random.nextFloat(20f, 40f)

        body.moveForward(-bodyLength)

        body.startRegularPolygon(cornerCount, 0.00001f, white)
        body.forward(0f)

        body.radius = bodyRadius * 0.6f
        body.forward(bodyLength / 2)

        body.radius = bodyRadius
        body.sides[topSide].material = green
        body.forward(bodyLength / 5)

        body.material = white
        val leftWing = body.sides[leftWingSide].turtle()
        val rightWing = body.sides[rightWingSide].turtle()
        body.forward(bodyLength)
        generateWing(leftWing, wingAngle, wingLength)
        generateWing(rightWing, wingAngle, wingLength)

        val tailFin = body.sides[topSide].turtle()
        val leftFin = body.sides[leftFinSide].turtle()
        val rightFin = body.sides[rightFinSide].turtle()
        body.radius = bodyRadius * 0.8f
        body.forward(bodyLength / 2)
        generateWing(tailFin, topFinAngle,topFinLength)
        generateWing(leftFin, sideFinAngle, sideFinLength)
        generateWing(rightFin, sideFinAngle, sideFinLength)

        body.radius = bodyRadius * 0.5f
        body.forward(bodyLength / 5)

        body.close()

        return body.end()
    }

    private fun generateWing(wing: Turtle, angle: Float, length: Float) {
        wing.forward(0f)
        wing.radius {_, old -> old * 0.6f}
        wing.forward(0f)

        wing.forwardDirection.rotate(wing.upDirection, angle)

        wing.radius {_, old -> old * 0.4f}
        wing.forward(0.9f * length)

        wing.radius {_, old -> old * 0.2f}
        wing.forward(0.1f * length)

        wing.close()
    }
}
