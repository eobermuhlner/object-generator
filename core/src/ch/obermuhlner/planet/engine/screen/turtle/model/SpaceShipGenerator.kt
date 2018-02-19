package ch.obermuhlner.planet.engine.screen.turtle.model

import ch.obermuhlner.planet.engine.construction.turtle.Turtle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.math.Vector3

class SpaceShipGenerator : TurtleModelGenerator {
    private val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
    private val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
    private val green = Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE))

    override fun generate(body: Turtle, random: Random): Model {
        val cornerCount = random.nextInt(2, 6) * 2 + 1

        val topSide = 0
        val leftWingSide = if (cornerCount <= 5) 1 else 2
        val rightWingSide = if (cornerCount <= 5) cornerCount - 1 else cornerCount - 2
        val leftSecondWingSide = leftWingSide + 1
        val rightSecondWingSide = rightWingSide - 1

        val finsOnSameSideAsWings = random.nextBoolean(0.5f)
        val hasSecondWings = cornerCount >= 9 && random.nextBoolean(0.5f)
        val hasTopFin = finsOnSameSideAsWings || random.nextBoolean(0.5f)
        val leftFinSide = if (finsOnSameSideAsWings) leftWingSide else 1
        val rightFinSide = if (finsOnSameSideAsWings) rightWingSide else cornerCount - 1
        val hasFrontFin = random.nextBoolean(0.6f)

        val frontFinLength = random.nextFloat(2f, 6f)
        val frontFinStartWidthFactor = random.nextFloat(0.2f, 0.6f)
        val frontFinMidWidthFactor = random.nextFloat(0.2f, frontFinStartWidthFactor)
        val frontFinFirstAngle = random.nextFloat(0f, -20f)
        val frontFinSecondAngle = random.nextFloat(0f, -2f)

        val wingLength = random.nextFloat(20f, 50f)
        val wingStartWidthFactor = random.nextFloat(0.4f, 0.8f)
        val wingMidWidthFactor = random.nextFloat(0.2f, wingStartWidthFactor)
        val wingFirstAngle = random.nextFloat(0f, -50f)
        val wingSecondAngle = random.nextFloat(0f, -20f)
        val wingUpAngle = random.nextFloat(-20f, 20f)

        val sideFinLength = random.nextFloat(10f, 30f)
        val sideFinStartWidthFactor = random.nextFloat(0.4f, 0.8f)
        val sideFinMidWidthFactor = random.nextFloat(0.2f, sideFinStartWidthFactor)
        val sideFinFirstAngle = random.nextFloat(0f, -50f)
        val sideFinSecondAngle = random.nextFloat(0f, -20f)
        val sideFinUpAngle = random.nextFloat(-20f, 20f)

        val topFinLength = random.nextFloat(5f, 15f)
        val topFinStartWidthFactor = random.nextFloat(0.4f, 0.8f)
        val topFinMinWidthFactor = random.nextFloat(0.2f, topFinStartWidthFactor)
        val topFinFirstAngle = random.nextFloat(0f, -50f)
        val topFinSecondAngle = random.nextFloat(0f, -20f)

        val bodyRadius = random.nextFloat(3f, 6f)
        val bodyLength = random.nextFloat(20f, 40f)

        body.moveForward(-bodyLength)

        body.startRegularPolygon(cornerCount, 0.00001f, white)
        body.forward(0f)

        body.radius = bodyRadius * random.nextFloat(0.5f, 0.8f)
        body.forward(bodyLength * random.nextFloat(0.3f, 0.6f))

        val frontLeftFin = if (hasFrontFin) body.sides[leftWingSide].turtle() else null
        val frontRightFin = if (hasFrontFin) body.sides[rightWingSide].turtle() else null
        body.radius = bodyRadius
        body.sides[topSide].material = green
        body.forward(bodyLength * random.nextFloat(0.2f, 0.4f))
        generateWing(frontLeftFin, frontFinLength, frontFinStartWidthFactor, frontFinMidWidthFactor, frontFinFirstAngle, frontFinSecondAngle, 0f)
        generateWing(frontRightFin, frontFinLength, frontFinStartWidthFactor, frontFinMidWidthFactor, frontFinFirstAngle, frontFinSecondAngle, 0f)

        body.material = white
        val leftWing = body.sides[leftWingSide].turtle()
        val rightWing = body.sides[rightWingSide].turtle()
        val leftSecondWing = if (hasSecondWings) body.sides[leftSecondWingSide].turtle() else null
        val rightSecondWing = if (hasSecondWings) body.sides[rightSecondWingSide].turtle() else null
        body.forward(bodyLength)
        generateWing(leftWing, wingLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, wingUpAngle)
        generateWing(rightWing, wingLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, -wingUpAngle)
        generateWing(leftSecondWing, wingLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, wingUpAngle)
        generateWing(rightSecondWing, wingLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, -wingUpAngle)

        val leftFin = body.sides[leftFinSide].turtle()
        val rightFin = body.sides[rightFinSide].turtle()
        val topFin = if (hasTopFin) body.sides[topSide].turtle() else null
        body.radius = bodyRadius * random.nextFloat(0.5f, 0.9f)
        body.forward(bodyLength * random.nextFloat(0.3f, 0.6f))
        generateWing(leftFin, sideFinLength, sideFinStartWidthFactor, sideFinMidWidthFactor, sideFinFirstAngle, sideFinSecondAngle, sideFinUpAngle)
        generateWing(rightFin, sideFinLength, sideFinStartWidthFactor, sideFinMidWidthFactor, sideFinFirstAngle, sideFinSecondAngle, -sideFinUpAngle)
        generateWing(topFin, topFinLength, topFinStartWidthFactor, topFinMinWidthFactor, topFinFirstAngle, topFinSecondAngle, 0f)

        body.radius = bodyRadius * random.nextFloat(0.3f, 0.5f)
        body.forward(bodyLength * random.nextFloat(0.1f, 0.3f))

        body.radius {_, old -> old * 0.9f }
        body.forward(-bodyLength * random.nextFloat(0.1f, 0.3f))

        body.material = red
        body.close()

        return body.end()
    }

    private fun generateWing(wing: Turtle?, length: Float, startWidthFactor: Float, midWidthFactor: Float, firstAngle: Float, secondAngle: Float, upAngle: Float) {
        if (wing == null) {
            return
        }

        wing.forward(0f)
        wing.radius {_, old -> old * startWidthFactor}
        wing.forward(0f)

        wing.forwardDirection.rotate(wing.upDirection, firstAngle)
        wing.radius {_, old -> old * midWidthFactor }
        wing.forward(0.6f * length)

        val sideDirection = Vector3(wing.forwardDirection).crs(wing.upDirection).nor()
        wing.forwardDirection.rotate(sideDirection, upAngle)
        wing.forwardDirection.rotate(wing.upDirection, secondAngle)
        wing.radius {_, old -> old * 0.2f}
        wing.forward(0.4f * length)

        wing.close()
    }
}
