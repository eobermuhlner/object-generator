package ch.obermuhlner.planet.engine.construction.turtle

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3

class Turtle(
        var center: Vector3 = Vector3(0f, 0f, 0f),
        var upDirection: Vector3 = Vector3.Z,
        var forwardDirection: Vector3 = Vector3.Y,
        private val builder: TurtleModelBuilder = TurtleModelBuilder()) {

    private var lastCornerPoints = listOf<MeshPartBuilder.VertexInfo>()
    private var corners = mutableListOf<Corner>()
    var sides = mutableListOf<Side>()

    fun regularPolygon(cornerCount: Int, radius: Float, material: Material) {
        regularPolygon(cornerCount, {_ -> radius}, {_ -> material})
    }

    fun regularPolygon(cornerCount: Int, radiusFunc: (Int) -> Float, materialFunc: (Int) -> Material) {
        polygon(cornerCount, {index -> 360f / cornerCount * index}, radiusFunc, materialFunc)
    }

    fun polygon(cornerCount: Int, angleFunc: (Int) -> Float, radiusFunc: (Int) -> Float, materialFunc: (Int) -> Material) {
        corners.clear()
        sides.clear()

        for (i in 0 until cornerCount) {
            corners.add(Corner(radiusFunc(i), angleFunc(i)))
            sides.add(Side(materialFunc(i)))
        }
    }

    fun polygon(materialFunc: (Int) -> Material, vararg points: Vector3) {
        corners.clear()
        sides.clear()

        for (pointIndex in points.indices) {
            val point = points[pointIndex]
            val radiusVec = Vector3(point).sub(center)
            val distance = radiusVec.len()
            val angle = angleBetween(upDirection, radiusVec, forwardDirection)
            corners.add(Corner(distance, angle))
            sides.add(Side(materialFunc(pointIndex)))
        }
    }

    private fun angleBetween(vector1: Vector3, vector2: Vector3, planeNormal: Vector3): Float {
        return MathUtils.atan2(Vector3(vector1).crs(vector2).dot(planeNormal), Vector3(vector1).dot(vector2)) * MathUtils.radiansToDegrees
    }

    private fun debugPoint(pos: Vector3, color: Color = Color.RED, size: Float = 1f) {
        builder.part(Material(ColorAttribute.createDiffuse(color))).box(pos.x, pos.y, pos.z, size, size, size)
    }

    private fun debugVector(pos: Vector3, vector: Vector3, color: Color = Color.BLUE) {
        debugLine(pos, Vector3(pos).add(vector))
    }

    private fun debugLine(pos1: Vector3, pos2: Vector3, color: Color = Color.BLUE) {
        builder.part(Material(ColorAttribute.createDiffuse(color)), GL20.GL_LINES).line(pos1, pos2)
    }

    var radius: Float
        get() {
            return this.corners.map { it.radius }.max() ?: 0f
        }
        set(value) {
            radius({_ -> value})
        }

    fun radius(radiusFunc: (Int) -> Float) {
        for (index in corners.indices) {
            corners[index].radius = radiusFunc(index)
        }
    }

    var angle: Float
        get() {
            return corners[0].angle
        }
        set(value) {
            angle({_ -> value})
        }

    fun angle(angleFunc: (Int) -> Float) {
        for (index in corners.indices) {
            corners[index].angle= angleFunc(index)
        }
    }

    var material: Material
        get() {
            return sides[0].material
        }
        set(value) {
            material({_ -> value})
        }

    fun material(materialFunc: (Int) -> Material) {
        for (index in sides.indices) {
            sides[index].material = materialFunc(index)
        }
    }

    var smooth: Boolean
        get() {
            return sides[0].smooth
        }
        set(value) {
            smooth({_ -> value})
        }

    fun smooth(smoothFunc: (Int) -> Boolean) {
        for (index in sides.indices) {
            sides[index].smooth = smoothFunc(index)
        }
    }

    fun rotate(angle: Float) {
        upDirection.rotate(forwardDirection, angle)
    }

    fun forward(step: Float): List<Turtle> {
        val subTurtles = mutableListOf<Turtle>()

        val delta = Vector3(forwardDirection).scl(step)
        center.add(delta)

        // TODO handle smooth/edged corners
        val cornerPoints = mutableListOf<MeshPartBuilder.VertexInfo>()
        for (corner in corners) {
            val cornerPoint = MeshPartBuilder.VertexInfo()
            cornerPoint.hasPosition = true
            cornerPoint.hasNormal = true
            cornerPoint.position.set(upDirection).rotate(forwardDirection, corner.angle)
            cornerPoint.normal.set(cornerPoint.position).nor()
            cornerPoint.position.scl(corner.radius).add(center)
            cornerPoints.add(cornerPoint)
        }

        if (lastCornerPoints.size > 0) {
            // TODO handle cases where number of sides has changed
            for (sideIndex in sides.indices) {
                val side = sides[sideIndex]

                val part = builder.part(side.material)
                val nextSideIndex = (sideIndex + 1) % sides.size

                val corner1 = lastCornerPoints[sideIndex]
                val corner2 = lastCornerPoints[nextSideIndex]
                val corner3 = cornerPoints[nextSideIndex]
                val corner4 = cornerPoints[sideIndex]

                when (side.type) {
                    SideType.Nothing -> {}
                    SideType.Turtle -> {
                        val vectorU = Vector3(corner2.position).sub(corner1.position).scl(0.5f)
                        val vectorV = Vector3(corner4.position).sub(corner1.position).scl(0.5f)
                        val center = Vector3(corner1.position).add(vectorU).add(vectorV)
                        val normal = Vector3(vectorU).crs(vectorV).nor()
                        val subTurtle = Turtle(center, vectorU.nor(), normal, builder)
                        subTurtle.polygon({_ -> side.material}, corner1.position, corner2.position, corner3.position, corner4.position)
                        subTurtle.forward(0f)
                        subTurtles.add(subTurtle)
                    }
                    SideType.Face -> {
                        corner1.setUV(0f, 0f)
                        corner2.setUV(0f, 1f)
                        corner3.setUV(1f, 1f)
                        corner4.setUV(1f, 0f)

                        if (side.smooth) {
                            // TODO set normal
                        } else {
                            val u = Vector3(corner2.position).sub(corner1.position)
                            val v = Vector3(corner4.position).sub(corner1.position)
                            corner1.normal.set(u).crs(v).nor()
                            corner2.normal.set(corner1.normal)
                            corner3.normal.set(corner1.normal)
                            corner4.normal.set(corner1.normal)
                        }
                        part.rect(corner1, corner2, corner3, corner4)
                    }
                }
            }
        }
        lastCornerPoints = cornerPoints

        return subTurtles
    }

    fun close() {
        radius = 0f
        forward(0f)
    }

    fun closeSingleSided(sideIndex: Int = 0) {
        val side = sides[sideIndex]
        val part = builder.part(side.material)

        when (lastCornerPoints.size) {
            3 -> part.triangle(lastCornerPoints[0], lastCornerPoints[1], lastCornerPoints[2])
            4 -> part.rect(lastCornerPoints[0], lastCornerPoints[1], lastCornerPoints[2], lastCornerPoints[3])
            else -> {
                // FIXME add polygon
            }
        }
    }

    fun end(): Model {
        return builder.modelBuilder.end()
    }
}

class Corner(
        var radius: Float,
        var angle: Float)

class Side(
        var material: Material,
        var smooth: Boolean = false,
        var type: SideType = SideType.Face)

class TurtleModelBuilder(
        val modelBuilder: ModelBuilder = ModelBuilder(),
        var attributes: Long = (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal or VertexAttributes.Usage.TextureCoordinates).toLong()) {

    init {
        modelBuilder.begin()
    }

    private var partIndex = 0
    private var currentMaterial: Material? = null
    private var currentPart: MeshPartBuilder? = null

    fun part(material: Material): MeshPartBuilder {
        return part(material, GL20.GL_TRIANGLES)
    }

    fun part(material: Material, primitiveType: Int): MeshPartBuilder {
        val oldPart = currentPart
        if (oldPart == null || currentMaterial == null || material != currentMaterial) {
            partIndex++
            val newPart = modelBuilder.part("$partIndex", primitiveType, attributes, material)
            currentPart = newPart
            currentMaterial = material
            return newPart
        }
        return oldPart
    }
}

enum class SideType {
    Nothing,
    Face,
    Turtle
}
