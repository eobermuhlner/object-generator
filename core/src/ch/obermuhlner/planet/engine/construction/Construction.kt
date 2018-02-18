package ch.obermuhlner.planet.engine.construction

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder

class Construction {
    var defaultColor = Color.WHITE
    var defaultSpecularColor: Color? = null
    val blocks = mutableListOf<Block>()
}

class Block(
        val x: Int,
        val y: Int,
        val z: Int,
        val shape: BlockShape,
        val colors: Array<Color?> = Array(6, { null }),
        val emissiveColors: Array<Color?> = Array(6, { null }),
        val specularColors: Array<Color?> = Array(6, { null }),
        val textures: Array<String?> = Array(6, { null }),
        val defaultTexture: String? = null,
        val rotX: Int = 0,
        val rotY: Int = 0,
        val rotZ: Int = 0) {

    companion object {
        const val Top = 0
        const val Bottom = 1
        const val Front = 2
        const val Back = 3
        const val Left = 4
        const val Right = 5
    }

    fun textures(textureName: String?) {
        for (i in textures.indices) {
            textures[i] = textureName
        }
    }

    fun colors(color: Color?) {
        for (i in colors.indices) {
            colors[i] = color
        }
    }

    fun emissiveColors(color: Color?) {
        for (i in emissiveColors.indices) {
            colors[i] = color
        }
    }

    fun specularColors(color: Color?) {
        for (i in specularColors.indices) {
            colors[i] = color
        }
    }
}

sealed class BlockShape(
        vararg val sides: SideShape)

sealed class SideShape(vararg val points : MeshPartBuilder.VertexInfo)

class EmptySide : SideShape()

class Rect(
        corner1 : MeshPartBuilder.VertexInfo,
        corner2 : MeshPartBuilder.VertexInfo,
        corner3 : MeshPartBuilder.VertexInfo,
        corner4 : MeshPartBuilder.VertexInfo) : SideShape(corner1, corner2, corner3, corner4)

class Triangle(
        corner1 : MeshPartBuilder.VertexInfo,
        corner2 : MeshPartBuilder.VertexInfo,
        corner3 : MeshPartBuilder.VertexInfo) : SideShape(corner1, corner2, corner3)

object Cube : BlockShape(
        Rect( // upDirection
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(0f, 1f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, +0.5f).setNor(0f, 1f, 0f).setUV(1.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, +0.5f, +0.5f).setNor(0f, 1f, 0f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, +0.5f, -0.5f).setNor(0f, 1f, 0f).setUV(0.0f, 0.0f)),
        Rect( // bottom
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(0f, -1f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(0f, -1f, 0f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, +0.5f).setNor(0f, -1f, 0f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(0f, -1f, 0f).setUV(1.0f, 1.0f)),
        Rect( // front
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, +0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(1.0f, 1.0f)),
        Rect( // back
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(0f, 0f, 1f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, +0.5f).setNor(0f, 0f, 1f).setUV(1.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, +0.5f, +0.5f).setNor(0f, 0f, 1f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, +0.5f).setNor(0f, 0f, 1f).setUV(0.0f, 0.0f)),
        Rect( // left
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(-1f, 0f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(-1f, 0f, 0f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, +0.5f).setNor(-1f, 0f, 0f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(-1f, 0f, 0f).setUV(1.0f, 1.0f)),
        Rect( // right
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(1f, 0f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, +0.5f, -0.5f).setNor(1f, 0f, 0f).setUV(1.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, +0.5f, +0.5f).setNor(1f, 0f, 0f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, +0.5f).setNor(1f, 0f, 0f).setUV(0.0f, 0.0f)))

object Diagonal : BlockShape(
        Rect( // upDirection (ramp)
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(0.5f, 0.5f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, +0.5f).setNor(0.5f, 0.5f, 0f).setUV(1.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, +0.5f).setNor(0.5f, 0.5f, 0f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(0.5f, 0.5f, 0f).setUV(0.0f, 0.0f)),
        Rect( // bottom
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(0f, -1f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(0f, -1f, 0f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, +0.5f).setNor(0f, -1f, 0f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(0f, -1f, 0f).setUV(1.0f, 1.0f)),
        Triangle( // front
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(1.0f, 1.0f)),
        Triangle( // back
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(0f, 0f, 1f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, +0.5f).setNor(0f, 0f, 1f).setUV(1.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, +0.5f).setNor(0f, 0f, 1f).setUV(0.0f, 0.0f)),
        Rect( // left
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(-1f, 0f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(-1f, 0f, 0f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, +0.5f).setNor(-1f, 0f, 0f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(-1f, 0f, 0f).setUV(1.0f, 1.0f)),
        EmptySide()) // right

object Corner : BlockShape(
        Triangle( // upDirection (corner)
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(0.5f, 0.5f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(0.5f, 0.5f, 0f).setUV(1.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(0.5f, 0.5f, 0f).setUV(0.0f, 0.0f)),
        Triangle( // bottom
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(0f, -1f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(0f, -1f, 0f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(0f, -1f, 0f).setUV(1.0f, 1.0f)),
        Triangle( // front
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, -0.5f, -0.5f).setNor(0f, 0f, -1f).setUV(1.0f, 1.0f)),
        EmptySide(), // back
        Triangle( // left
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, -0.5f).setNor(-1f, 0f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, -0.5f, +0.5f).setNor(-1f, 0f, 0f).setUV(0.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(-1f, 0f, 0f).setUV(1.0f, 1.0f)),
        EmptySide()) // right

object Rectangle : BlockShape(
        Rect( // upDirection
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, -0.5f).setNor(0f, 1f, 0f).setUV(1.0f, 0.0f),
                MeshPartBuilder.VertexInfo().setPos(-0.5f, +0.5f, +0.5f).setNor(0f, 1f, 0f).setUV(1.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, +0.5f, +0.5f).setNor(0f, 1f, 0f).setUV(0.0f, 1.0f),
                MeshPartBuilder.VertexInfo().setPos(+0.5f, +0.5f, -0.5f).setNor(0f, 1f, 0f).setUV(0.0f, 0.0f)),
        EmptySide(), // bottom
        EmptySide(), // front
        EmptySide(), // back
        EmptySide(), // left
        EmptySide()) // right
