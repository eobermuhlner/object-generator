package ch.obermuhlner.planet.engine.util

class Random {
    private val random = java.util.Random()

    fun nextFloat(): Float {
        return random.nextFloat()
    }

    fun nextFloat(min: Float, max: Float): Float{
        return random.nextFloat() * (max-min) + min
    }

    fun nextInt(min: Int, max: Int): Int {
        if (min == max) {
            return min
        }
        return random.nextInt(max-min) + min
    }

    fun nextBoolean(probability: Float): Boolean {
        return nextFloat() < probability
    }

    fun <T> next(vararg elements: T): T {
        val index = nextInt(0, elements.size)
        return elements[index]
    }
}