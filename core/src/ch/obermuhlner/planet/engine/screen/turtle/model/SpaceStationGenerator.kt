package ch.obermuhlner.planet.engine.screen.turtle.model

import ch.obermuhlner.obj3d.turtle.Turtle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute

class SpaceStationGenerator : TurtleModelGenerator {
    private val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))

    override fun generate(turtle: Turtle, random: Random): Model {
        val cornerCount = 6

        turtle.moveForward(-20f)

        turtle.regularPolygon(cornerCount, 0.000001f, white)
        turtle.smooth = false
        turtle.forward(0f)

        turtle.radius = 1f
        turtle.forward(0f)

        turtle.radius = 3f
        turtle.forward(1f)

        turtle.forward(5f)

        val spokes = turtle.sides.map { side -> side.turtle() }
        turtle.forward(3f)
        spokes.forEach { spoke -> generateSpoke(spoke, random) }

        turtle.forward(5f)

        turtle.radius = 0.5f
        turtle.forward(0f)

        turtle.forward(1f)

        turtle.radius = 2f
        turtle.forward(0f)

        turtle.radius = 7f
        turtle.forward(1f)

        val hangars = turtle.sides.map { side -> side.turtle() }
        turtle.forward(2f)
        hangars.forEach { hangar -> generateHangar(hangar, random) }

        turtle.radius = 2f
        turtle.forward(1f)

        turtle.close()

        return turtle.end()
    }

    private fun generateSpoke(spoke: Turtle, random: Random) {
        spoke.radius *= 0.5f
        spoke.forward(2f)

        spoke.forward(10f)

        spoke.radius *= 4f
        spoke.forward(0.5f)

        val ringLeft = spoke.sides[1].turtle()
        val ringRight = spoke.sides[3].turtle()
        spoke.forward(1f)
        generateRingPart(ringLeft, random)
        generateRingPart(ringRight, random)

        spoke.radius /= 4f
        spoke.forward(0.5f)

        spoke.close()
    }

    private fun generateRingPart(ringPart: Turtle, random: Random) {
        ringPart.forward(6.5f)
        ringPart.close()
    }

    private fun generateHangar(hangar: Turtle, random: Random) {
        hangar.radius { _, radius -> radius * 0.5f}
        hangar.forward(0f)

        hangar.forward(-3f)
        hangar.close()
    }
}
