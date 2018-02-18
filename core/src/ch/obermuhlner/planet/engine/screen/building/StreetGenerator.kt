package ch.obermuhlner.planet.engine.screen.building

import ch.obermuhlner.planet.engine.construction.Block
import ch.obermuhlner.planet.engine.construction.BlockTexture
import ch.obermuhlner.planet.engine.construction.Construction
import ch.obermuhlner.planet.engine.construction.Rectangle
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.graphics.Color

class StreetGenerator(private val random: Random) {

    fun create(gridWidth: Int, gridLength: Int, gridCountX: Int, gridCountZ: Int, streetSize: Int) : Construction {
        val construction = Construction()

        val roadTexture = BlockTexture.Road2.name
        val crossRoadTexture = BlockTexture.RoadCrossing2.name

        val y = 0
        for (x in 0 until gridWidth * gridCountX + streetSize) {
            for (z in 0 until gridLength * gridCountZ + streetSize) {
                val isRoadX = x % gridWidth < streetSize
                val isRoadZ = z % gridLength < streetSize

                if (isRoadX && isRoadZ) {
                    val block = Block(x, y, z, Rectangle)
                    block.textures[Block.Companion.Top] = crossRoadTexture
                    block.specularColors[Block.Companion.Top] = Color.LIGHT_GRAY
                    //block.emissiveColors[Block.Companion.Top] = Color.YELLOW
                    construction.blocks.add(block)
                } else if (isRoadX) {
                    val block = Block(x, y, z, Rectangle)
                    block.textures[Block.Companion.Top] = roadTexture
                    block.specularColors[Block.Companion.Top] = Color.LIGHT_GRAY
                    //block.emissiveColors[Block.Companion.Top] = Color.YELLOW
                    construction.blocks.add(block)
                } else if (isRoadZ) {
                    val block = Block(x, y, z, Rectangle, rotY = 1)
                    block.textures[Block.Companion.Top] = roadTexture
                    block.specularColors[Block.Companion.Top] = Color.LIGHT_GRAY
                    //block.emissiveColors[Block.Companion.Top] = Color.YELLOW
                    construction.blocks.add(block)
                }
            }
        }

        return construction
    }
}
