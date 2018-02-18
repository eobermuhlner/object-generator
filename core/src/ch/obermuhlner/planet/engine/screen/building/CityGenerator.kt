package ch.obermuhlner.planet.engine.screen.building

import ch.obermuhlner.planet.engine.util.Random
import ch.obermuhlner.planet.engine.construction.ConstructionInstance



class CityGenerator(private val random: Random) {
    private val buildingGenerator = BuildingGenerator(random)
    private val streetGenerator = StreetGenerator(random)
    private val generateInnerYard = true

    fun create(): List<ConstructionInstance> {
        val result = mutableListOf<ConstructionInstance>()

        val quartersPerCity = 4

        val buildingSize = 2
        val plotSize = buildingSize + 0

        val plotsPerQuarter = 4
        val streetSize = 2
        val quarterSize = plotsPerQuarter * plotSize + streetSize

        val streetX = (- quartersPerCity / 2) * quarterSize - streetSize
        val streetZ = streetX
        result += ConstructionInstance(streetGenerator.create(quarterSize, quarterSize, quartersPerCity, quartersPerCity, streetSize), streetX, -1, streetZ)

        val innerYardSize = plotsPerQuarter - 2

        for (quarterX in 0 until quartersPerCity) {
            for (quarterZ in 0 until quartersPerCity) {
                val buildingHeight = 2*quarterX + 2*quarterZ + 2

                for (plotX in 0 until plotsPerQuarter) {
                    for (plotZ in 0 until plotsPerQuarter) {
                        val x = (quarterX - quartersPerCity / 2) * quarterSize + plotX * plotSize
                        val y = 0
                        val z = (quarterZ - quartersPerCity / 2) * quarterSize + plotZ * plotSize
                        if (generateInnerYard && isInnerYardBlock(plotX, plotsPerQuarter) && isInnerYardBlock(plotZ, plotsPerQuarter)) {
                            val innerYard = buildingGenerator.create(buildingSize * innerYardSize, 0, buildingSize * innerYardSize)
                            result += ConstructionInstance( innerYard, x, 0, z)
                        } else if (generateInnerYard && isInnerYardNothing(plotX, plotsPerQuarter) && isInnerYardNothing(plotZ, plotsPerQuarter)) {
                            // nothing
                        } else {
                            val building = buildingGenerator.create(buildingSize, buildingHeight, buildingSize)
                            result += ConstructionInstance( building, x, 0, z)
                        }
                    }
                }
            }
        }

        return result
    }

    private fun isInnerYardBlock(plotIndex: Int, plotsPerQuarter: Int): Boolean {
        return plotIndex == 1 && plotIndex < plotsPerQuarter - 1
    }

    private fun isInnerYardNothing(plotIndex: Int, plotsPerQuarter: Int): Boolean {
        return plotIndex > 0 && plotIndex < plotsPerQuarter - 1
    }
}