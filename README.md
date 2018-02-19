# object-generator

The goal of this project is to write a prototype for API that could be used in procedural generation of 3D objects.

The API is based on 3D turtle graphics and allows to create a 3D mesh in a minimum number of lines of code.

![Generated Spaceship 01](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship01.png "Generated Spaceship 01")
![Generated Spaceship 02](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship02.png "Generated Spaceship 02")
![Generated Spaceship 03](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship03.png "Generated Spaceship 03")
![Generated Spaceship 04](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship04.png "Generated Spaceship 04")
![Generated Spaceship 05](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship05.png "Generated Spaceship 05")
![Generated Spaceship 06](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship06.png "Generated Spaceship 06")
![Generated Spaceship 07](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship07.png "Generated Spaceship 07")
![Generated Spaceship 08](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship08.png "Generated Spaceship 08")
![Generated Spaceship 09](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship09.png "Generated Spaceship 09")
![Generated Spaceship 10](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship10.png "Generated Spaceship 10")
![Generated Spaceship 11](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship11.png "Generated Spaceship 11")
![Generated Spaceship 12](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship12.png "Generated Spaceship 12")
![Generated Spaceship 13](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship13.png "Generated Spaceship 13")
![Generated Spaceship 14](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship14.png "Generated Spaceship 14")
![Generated Spaceship 15](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship15.png "Generated Spaceship 15")
![Generated Spaceship 16](https://raw.githubusercontent.com/eobermuhlner/object-generator/master/docs/images/spaceship16.png "Generated Spaceship 16")

```kotlin
class SpaceShipGenerator : TurtleModelGenerator {
    private val white = Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE))
    private val red = Material(ColorAttribute.createDiffuse(Color.RED), ColorAttribute.createSpecular(Color.WHITE))
    private val green = Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(Color.WHITE))

    override fun generate(body: Turtle, random: Random): Model {
        val cornerCount = random.nextInt(3, 5) * 2 + 1
        val topSide = 0
        val leftWingSide = 2
        val rightWingSide = cornerCount - 2
        val finsOnSameSideAsWings = random.nextBoolean(0.5f)
        val hasTopFin = finsOnSameSideAsWings || random.nextBoolean(0.5f)
        val leftFinSide = if (finsOnSameSideAsWings) leftWingSide else 1
        val rightFinSide = if (finsOnSameSideAsWings) rightWingSide else cornerCount - 1

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

        body.radius = bodyRadius
        body.sides[topSide].material = green
        body.forward(bodyLength * random.nextFloat(0.2f, 0.4f))

        body.material = white
        val leftWing = body.sides[leftWingSide].turtle()
        val rightWing = body.sides[rightWingSide].turtle()
        body.forward(bodyLength)
        generateWing(leftWing, wingLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, wingUpAngle)
        generateWing(rightWing, wingLength, wingStartWidthFactor, wingMidWidthFactor, wingFirstAngle, wingSecondAngle, -wingUpAngle)

        val topFin = if (hasTopFin) body.sides[topSide].turtle() else null
        val leftFin = body.sides[leftFinSide].turtle()
        val rightFin = body.sides[rightFinSide].turtle()
        body.radius = bodyRadius * random.nextFloat(0.5f, 0.9f)
        body.forward(bodyLength * random.nextFloat(0.3f, 0.6f))
        if (topFin != null) {
            generateWing(topFin, topFinLength, topFinStartWidthFactor, topFinMinWidthFactor, topFinFirstAngle, topFinSecondAngle, 0f)
        }
        generateWing(leftFin, sideFinLength, sideFinStartWidthFactor, sideFinMidWidthFactor, sideFinFirstAngle, sideFinSecondAngle, sideFinUpAngle)
        generateWing(rightFin, sideFinLength, sideFinStartWidthFactor, sideFinMidWidthFactor, sideFinFirstAngle, sideFinSecondAngle, -sideFinUpAngle)

        body.radius = bodyRadius * random.nextFloat(0.3f, 0.5f)
        body.forward(bodyLength * random.nextFloat(0.1f, 0.3f))

        body.radius {_, old -> old * 0.9f }
        body.forward(-bodyLength * random.nextFloat(0.1f, 0.3f))

        body.material = red
        body.close()

        return body.end()
    }

    private fun generateWing(wing: Turtle, length: Float, startWidthFactor: Float, midWidthFactor: Float, firstAngle: Float, secondAngle: Float, upAngle: Float) {
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
```
