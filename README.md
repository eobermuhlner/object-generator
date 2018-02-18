# object-generator

The goal of this project is to write a prototype for API that could be used in procedural generation of 3D objects.

The API is based on 3D turtle graphics and allows to create a 3D mesh in a minimum number of lines of code.

![Generated Spaceship](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship1.png "Generated Spaceship")

```kotlin
class SpaceShipGenerator : TurtleModelGenerator {
    private val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
    private val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
    private val green = Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE))

    override fun generate(body: Turtle, random: Random): Model {
        val cornerCount = 7
        val topSide = 0
        val leftWingSide = 2
        val rightWingSide = cornerCount - 2
        val leftFinSide = leftWingSide // 1
        val rightFinSide = rightWingSide // cornerCount - 1

        val bodyRadius = 5f

        body.moveForward(-30f)

        body.startRegularPolygon(cornerCount, 0.00001f, white)
        body.forward(0f)

        body.radius = bodyRadius * 0.6f
        body.forward(10f)

        body.radius = bodyRadius
        val cockpit = body.sides[topSide].turtle()
        body.forward(5f)
        generateCockpit(cockpit, random)

        val leftWing = body.sides[leftWingSide].turtle()
        val rightWing = body.sides[rightWingSide].turtle()
        body.forward(25f)
        generateWing(leftWing, 1f)
        generateWing(rightWing, 1f)

        val tailFin = body.sides[topSide].turtle()
        val leftFin = body.sides[leftFinSide].turtle()
        val rightFin = body.sides[rightFinSide].turtle()
        body.radius = bodyRadius * 0.8f
        body.forward(15f)
        generateFin(tailFin, 1f)
        generateFin(leftFin, 2f)
        generateFin(rightFin, 2f)

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

    private fun generateWing(wing: Turtle, length: Float) {
        wing.forward(0f)
        wing.radius {_, old -> old * 0.6f}
        wing.forward(0f)

        wing.forwardDirection.rotate(wing.upDirection, -50f)

        wing.radius {_, old -> old * 0.4f}
        wing.forward(25f * length)

        wing.radius {_, old -> old * 0.2f}
        wing.forward(3f * length)

        wing.close()
    }

    private fun generateFin(fin: Turtle, length: Float) {
        fin.forward(0f)
        fin.radius { _, old -> old * 0.6f}
        fin.forward(0f)

        fin.forwardDirection.rotate(fin.upDirection, -45f)

        fin.radius { _, old -> old * 0.4f}
        fin.forward(5f * length)

        fin.radius { _, old -> old * 0.2f}
        fin.forward(1f * length)

        fin.close()
    }
}
```
