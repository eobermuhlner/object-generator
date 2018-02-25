package ch.obermuhlner.planet.engine.screen.turtle.model

import ch.obermuhlner.obj3d.turtle.Turtle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.graphics.g3d.Model

interface TurtleModelGenerator {
    fun generate(turtle: Turtle, random: Random): Model
}
