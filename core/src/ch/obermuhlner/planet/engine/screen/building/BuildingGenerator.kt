package ch.obermuhlner.planet.engine.screen.building

import ch.obermuhlner.planet.engine.construction.*
import ch.obermuhlner.planet.engine.util.Random
import com.badlogic.gdx.graphics.Color

class BuildingGenerator(private val random: Random) {
    fun create(maxWidth: Int, maxHeight: Int, maxLength: Int) : Construction {
        if (maxHeight == 0 || random.nextBoolean(0.05f)) {
            return createOpenSpace(maxWidth, maxHeight, maxLength)
        }

        return createTraditionalHouse(maxWidth, maxHeight, maxLength)
    }


    private fun createOpenSpace(maxWidth: Int, maxHeight: Int, maxLength: Int) : Construction  {
        val width = maxWidth
        val length = maxLength

        val gray = random.nextFloat(0.2f, 1.0f)
        val spaceColor = Color(gray, gray, gray, 1f)

        val construction = Construction()
        construction.defaultColor = spaceColor

        val spaceTexture = random.next(BlockTexture.FloorBrick1.name)

        val y = -1
        for (x in 0 until width) {
            for (z in 0 until length) {
                val block = Block(x, y, z, Rectangle)
                block.textures[Block.Companion.Top] = spaceTexture
                construction.blocks.add(block)
            }
        }

        return construction
    }
    private fun createTraditionalHouse(maxWidth: Int, maxHeight: Int, maxLength: Int) : Construction {
        val width = maxWidth // random.nextInt(1, maxWidth)
        val height = random.nextInt(1, maxHeight)
        val length = maxLength // random.nextInt(1, maxLength)

        val construction = Construction()

        val gray = random.nextFloat(0.2f, 1.0f)
        val buildingColor = Color(gray, gray, gray, 1f)
        construction.defaultColor = buildingColor
        construction.defaultSpecularColor = buildingColor

        val skyscraper = height >= 5

        val wallTexture = if (skyscraper) {
            random.next(BlockTexture.WindowGridSmall.name, BlockTexture.WindowGridMedium.name, BlockTexture.WindowGridLarge.name)
        } else {
            random.next(BlockTexture.Brick1.name, BlockTexture.Brick2.name)
        }

        for (x in 0 until width) {
            for (y in 0 until height) {
                for (z in 0 until length) {
                    val block = Block(x, y, z, Cube)
                    block.textures(wallTexture)
                    block.textures[Block.Companion.Top] = BlockTexture.Concrete1.name
                    block.textures[Block.Companion.Bottom] = BlockTexture.Concrete1.name
                    construction.blocks.add(block)
                }
            }
        }

        if (!skyscraper) {
            if (random.nextBoolean(0.5f)) {
                simpleRoof(construction, wallTexture, width, height, length)
            } else {
                simpleRoofSideway(construction, wallTexture, width, height, length)
            }
        }

        return construction
    }

    private fun simpleRoof(construction: Construction, wallTexture: String, width: Int, height: Int, length: Int) {
        val roofColor = Color(random.nextFloat(0.2f, 1.0f), random.nextFloat(0.2f, 1.0f), random.nextFloat(0.2f, 1.0f), 1f)
        val roofTexture = random.next(BlockTexture.RoofTile1.name, BlockTexture.RoofTile2.name)

        for (x in 0 until width) {
            for (z in 0 until length) {
                val block = when(z) {
                    0 -> Block(x, height, z, Diagonal, rotY = 1)
                    length - 1 -> Block(x, height, z, Diagonal, rotY = 3)
                    else -> Block(x, height, z, Cube)
                }
                block.colors[Block.Companion.Top] = roofColor
                block.specularColors[Block.Companion.Top] = roofColor
                block.textures(wallTexture)
                block.textures[Block.Companion.Top] = roofTexture
                construction.blocks.add(block)
            }
        }
    }

    private fun simpleRoofSideway(construction: Construction, wallTexture: String, width: Int, height: Int, length: Int) {
        val roofColor = Color(random.nextFloat(0.2f, 1.0f), random.nextFloat(0.2f, 1.0f), random.nextFloat(0.2f, 1.0f), 1f)
        val roofTexture = random.next(BlockTexture.RoofTile1.name, BlockTexture.RoofTile2.name)

        for (x in 0 until width) {
            for (z in 0 until length) {
                val block = when(x) {
                    0 -> Block(x, height, z, Diagonal, rotY = 2)
                    width - 1 -> Block(x, height, z, Diagonal, rotY = 0)
                    else -> Block(x, height, z, Cube)
                }
                block.colors[Block.Companion.Top] = roofColor
                block.specularColors[Block.Companion.Top] = roofColor
                block.textures(wallTexture)
                block.textures[Block.Companion.Top] = roofTexture
                construction.blocks.add(block)
            }
        }
    }
}
