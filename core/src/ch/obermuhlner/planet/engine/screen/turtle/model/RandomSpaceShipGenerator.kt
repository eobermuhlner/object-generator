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

class RandomSpaceShipGenerator : TurtleModelGenerator {
    val textureDiffuse = Texture(Gdx.files.internal("textures/DeepDamageMetal_diffuse.jpg").path())
    val textureNormal = Texture(Gdx.files.internal("textures/DeepDamageMetal_normal.jpg").path())
    init {
        textureDiffuse.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
        textureNormal.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
    }

    private val white = Material(TextureAttribute.createDiffuse(textureDiffuse), TextureAttribute.createNormal(textureNormal), ColorAttribute.createSpecular(Color.WHITE))
    //private val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
    private val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
    private val green = Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE))
    private val black = Material(ColorAttribute.createDiffuse(Color.BLACK), ColorAttribute.createSpecular(Color.WHITE))

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

        val frontFinConnectorLength = random.nextFloat(0.0f, 0.1f)
        val frontFinMainLength = random.nextFloat(2f, 6f) * 0.6f
        val frontFinTipLength = random.nextFloat(2f, 6f) * 0.4f
        val frontFinStartWidthFactor = random.nextFloat(0.2f, 0.6f)
        val frontFinMidWidthFactor = random.nextFloat(0.2f, frontFinStartWidthFactor)
        val frontFinFirstAngle = random.nextFloat(0f, -30f)
        val frontFinSecondAngle = random.nextFloat(0f, -2f)

        val wingConnectorLength = random.nextFloat(0.0f, 1.0f)
        val wingMainLength = random.nextFloat(20f, 50f) * 0.6f
        val wingTipLength = random.nextFloat(20f, 50f) * 0.4f
        val wingStartWidthFactor = random.nextFloat(0.4f, 0.8f)
        val wingMidWidthFactor = random.nextFloat(0.2f, wingStartWidthFactor)
        val wingFirstAngle = random.nextFloat(0f, -50f)
        val wingSecondAngle = random.nextFloat(0f, -20f)
        val wingUpAngle = if (random.nextBoolean(0.5f)) 0f else random.nextFloat(-20f, 20f)
        val secondWingUpAngle = if (random.nextBoolean(0.8f)) -wingUpAngle else wingUpAngle

        val sideFinConnectorLength = random.nextFloat(0.0f, 1.0f)
        val sideFinMainLength = random.nextFloat(10f, 30f) * 0.6f
        val sideFinTipLength = random.nextFloat(10f, 30f) * 0.4f
        val sideFinStartWidthFactor = random.nextFloat(0.4f, 0.8f)
        val sideFinMidWidthFactor = random.nextFloat(0.2f, sideFinStartWidthFactor)
        val sideFinFirstAngle = random.nextFloat(0f, -50f)
        val sideFinSecondAngle = random.nextFloat(0f, -20f)
        val sideFinUpAngle = if (random.nextBoolean(0.8f)) 0f else random.nextFloat(-20f, 20f)

        val topFinConnectorLength = random.nextFloat(0.0f, 0.5f)
        val topFinMainLength = random.nextFloat(5f, 15f) * 0.6f
        val topFinTipLength = random.nextFloat(5f, 15f) * 0.4f
        val topFinStartWidthFactor = random.nextFloat(0.4f, 0.8f)
        val topFinMinWidthFactor = random.nextFloat(0.2f, topFinStartWidthFactor)
        val topFinFirstAngle = random.nextFloat(0f, -50f)
        val topFinSecondAngle = random.nextFloat(0f, -20f)

        val bodyRadius = random.nextFloat(3f, 6f)
        val bodyLength = random.nextFloat(20f, 40f)

        body.uvScale.set(0.1f, 0.1f)

        body.moveForward(-bodyLength)

        body.regularPolygon(cornerCount, 0.000001f, white)
        body.smooth = false
        body.forward(0f)

        body.radius = bodyRadius * random.nextFloat(0.01f, 0.2f)
        body.forward(bodyLength * random.nextFloat(0.1f, 0.2f))

        body.radius = bodyRadius * random.nextFloat(0.5f, 0.8f) + 0.2f
        body.forward(bodyLength * random.nextFloat(0.3f, 0.6f))

        val frontLeftFin = if (hasFrontFin) body.sides[leftWingSide].turtle() else null
        val frontRightFin = if (hasFrontFin) body.sides[rightWingSide].turtle() else null
        val cockpit = body.sides[topSide].turtle()
        body.radius = bodyRadius
        body.forward(bodyLength * random.nextFloat(0.2f, 0.4f))
        generateWing(frontLeftFin, frontFinConnectorLength, frontFinMainLength, frontFinTipLength, frontFinStartWidthFactor, frontFinMidWidthFactor, frontFinFirstAngle, frontFinSecondAngle, 0f)
        generateWing(frontRightFin, frontFinConnectorLength, frontFinMainLength, frontFinTipLength, frontFinStartWidthFactor, frontFinMidWidthFactor, frontFinFirstAngle, frontFinSecondAngle, 0f)
        generateCockpit(cockpit)

        body.material = white
        val leftWing = body.sides[leftWingSide].turtle()
        val rightWing = body.sides[rightWingSide].turtle()
        val leftSecondWing = if (hasSecondWings) body.sides[leftSecondWingSide].turtle() else null
        val rightSecondWing = if (hasSecondWings) body.sides[rightSecondWingSide].turtle() else null
        body.forward(bodyLength)
        generateWing(leftWing, wingConnectorLength, wingMainLength, wingTipLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, wingUpAngle)
        generateWing(rightWing, wingConnectorLength, wingMainLength, wingTipLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, -wingUpAngle)
        generateWing(leftSecondWing, wingConnectorLength, wingMainLength, wingTipLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, secondWingUpAngle)
        generateWing(rightSecondWing, wingConnectorLength, wingMainLength, wingTipLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, -secondWingUpAngle)

        val leftFin = body.sides[leftFinSide].turtle()
        val rightFin = body.sides[rightFinSide].turtle()
        val topFin = if (hasTopFin) body.sides[topSide].turtle() else null
        body.radius = bodyRadius * random.nextFloat(0.5f, 0.9f)
        body.forward(bodyLength * random.nextFloat(0.3f, 0.6f))
        generateWing(leftFin, sideFinConnectorLength, sideFinMainLength, sideFinTipLength, sideFinStartWidthFactor, sideFinMidWidthFactor, sideFinFirstAngle, sideFinSecondAngle, sideFinUpAngle)
        generateWing(rightFin, sideFinConnectorLength, sideFinMainLength, sideFinTipLength, sideFinStartWidthFactor, sideFinMidWidthFactor, sideFinFirstAngle, sideFinSecondAngle, -sideFinUpAngle)
        generateWing(topFin, topFinConnectorLength, topFinMainLength, topFinTipLength, topFinStartWidthFactor, topFinMinWidthFactor, topFinFirstAngle, topFinSecondAngle, 0f)

        body.radius = bodyRadius * random.nextFloat(0.3f, 0.5f)
        body.forward(bodyLength * random.nextFloat(0.1f, 0.3f))

        body.radius {_, old -> old * 0.9f }
        body.forward(-bodyLength * random.nextFloat(0.1f, 0.3f))

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
