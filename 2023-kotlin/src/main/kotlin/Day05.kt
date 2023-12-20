data class GardenRange(val sourceRangeStart: Long, val sourceRangeEnd: Long, val destinationRageStart: Long) : GenericGardenRange {
    companion object {
        /**
         * 50 98 2 => GardenRange(50, 51, 98)
         */
        fun fromString(input: String): GardenRange {
            val (destinationRageStart, sourceRangeStart, rangeLength) = input.split(" ").map { it.toLong() }
            return GardenRange(sourceRangeStart, sourceRangeStart + rangeLength - 1L, destinationRageStart)
        }
    }

    fun doesInclude(value: Long): Boolean {
        return value in sourceRangeStart..sourceRangeEnd
    }

    override fun map(sourceValue: Long): Long {
        return destinationRageStart + (sourceValue - sourceRangeStart)
    }
}

interface GenericGardenRange {
    fun map(sourceValue: Long): Long
}

class IdentityGardenRange : GenericGardenRange {
    override fun map(sourceValue: Long): Long {
        return sourceValue
    }
}

val identityGardenRange = IdentityGardenRange()

class GardenRangeMap(val ranges: List<GardenRange>) {
    private val cache: MutableMap<Long, Long> = mutableMapOf()
    private val minRangeStart = ranges.minOf { it.sourceRangeStart }
    private val maxRangeEnd = ranges.maxOf { it.sourceRangeEnd }

    companion object {
        /**
         * ["50 98 2", "52 50 48"] =>
         * GardenRangeMap([
         *      GardenRange(50, 51, 98),
         *      GardenRange(52, 99, 50)
         * ])
         */
        fun fromString(input: List<String>): GardenRangeMap {
            return GardenRangeMap(input.filter { it.isNotEmpty() }.map { GardenRange.fromString(it) })
        }

        fun fromString(input: String): GardenRangeMap {
            return fromString(input.split("\n").drop(1))
        }
    }

    fun map(sourceValue: Long): Long {
        val destinationValue = cache[sourceValue] ?: when {
            sourceValue < minRangeStart -> sourceValue
            sourceValue > maxRangeEnd -> sourceValue
            else -> {
                val matchingRange = ranges.firstOrNull { it.doesInclude(sourceValue) } ?: identityGardenRange
                return matchingRange.map(sourceValue)
            }
        }
        cache[sourceValue] = destinationValue
        return destinationValue
    }
}

data class Almanac(
        val seeds: List<Long>,
        val toSoil: GardenRangeMap,
        val toFertilizer: GardenRangeMap,
        val toWater: GardenRangeMap,
        val toLight: GardenRangeMap,
        val toTemperature: GardenRangeMap,
        val toHumidity: GardenRangeMap,
        val toLocation: GardenRangeMap,
        val minLocations: List<Long> = listOf()
) {
    companion object {
        private fun parseRanges(mappingGroups: List<String>): Almanac {
            val toSoil = GardenRangeMap.fromString(mappingGroups.first { it.contains("-to-soil") })
            val toFertilizer = GardenRangeMap.fromString(mappingGroups.first { it.contains("-to-fertilizer") })
            val toWater = GardenRangeMap.fromString(mappingGroups.first { it.contains("-to-water") })
            val toLight = GardenRangeMap.fromString(mappingGroups.first { it.contains("-to-light") })
            val toTemp = GardenRangeMap.fromString(mappingGroups.first { it.contains("-to-temperature") })
            val toHumidity = GardenRangeMap.fromString(mappingGroups.first { it.contains("-to-humidity") })
            val toLocation = GardenRangeMap.fromString(mappingGroups.first { it.contains("-to-location") })

            return Almanac(
                    listOf(),
                    toSoil,
                    toFertilizer,
                    toWater,
                    toLight,
                    toTemp,
                    toHumidity,
                    toLocation
            )
        }

        fun fromString(input: String): Almanac {
            val mappingGroups = input.split("\n\n")
            val seeds = mappingGroups.first()
                    .replace("seeds: ", "")
                    .split(" ")
                    .map { intStr -> intStr.toLong() }
            return parseRanges(mappingGroups).copy(seeds = seeds)
        }

        fun fromStringSeedRange(input: String): Almanac {
            val mappingGroups = input.split("\n\n")
            val almanac = parseRanges(mappingGroups)

            val seedsStr = mappingGroups.first()
            val seedsDefMatch = Regex(":?(\\d+ \\d+)").findAll(seedsStr).toList()
                    .map { matchRes ->
                        val (seedRangeStart, seedRangeLength) = matchRes.groups.first()!!.value.split(" ").map { it.toLong() }
                        Pair(seedRangeStart, seedRangeLength)
                    }

            val minLocations = seedsDefMatch
                    .map { (seedRangeStart, seedRangeLength) ->
                        var minSoFar = Long.MAX_VALUE
                        for (seed in seedRangeStart..<seedRangeStart + seedRangeLength) {
                            val soil = almanac.toSoil.map(seed)
                            val fertilizer = almanac.toFertilizer.map(soil)
                            val water = almanac.toWater.map(fertilizer)
                            val light = almanac.toLight.map(water)
                            val temp = almanac.toTemperature.map(light)
                            val humidity = almanac.toHumidity.map(temp)
                            val location = almanac.toLocation.map(humidity)
                            if (location < minSoFar) {
                                minSoFar = location
                            }
                        }

                        minSoFar
                    }

            return almanac.copy(minLocations = minLocations)
        }
    }

    fun getSeedLocation(seed: Long): Long {
        val soil = toSoil.map(seed)
        val fertilizer = toFertilizer.map(soil)
        val water = toWater.map(fertilizer)
        val light = toLight.map(water)
        val temp = toTemperature.map(light)
        val humidity = toHumidity.map(temp)
        return toLocation.map(humidity)
    }
}

class Day05(sourceFileName: String = "Day05") : PuzzleSolutionNonSplitInput {
    override val input: String = readInputNotSplit(sourceFileName)
    override fun part1(input: String): String {
        val almanac = Almanac.fromString(input)
        val locations = almanac.seeds.map { seed -> almanac.getSeedLocation(seed) }

        return locations.min().toInt().toString()
    }

    override fun part2(input: String): String {
        val almanac = Almanac.fromStringSeedRange(input)
        return almanac.minLocations.min().toInt().toString()
    }
}

fun main() {
    val solution = Day05()

    // Test GardenRange
    val testGardenRange1 = GardenRange.fromString("50 98 2")
    check(testGardenRange1 == GardenRange(98, 99, 50))
    check(testGardenRange1.map(98) == 50L)
    val testGardenRange2 = GardenRange.fromString("52 50 48")
    check(testGardenRange2 == GardenRange(50, 97, 52))
    check(testGardenRange2.map(53) == 55L)

    // Test GardenRangeMap
    val testGardenRangeMap = GardenRangeMap.fromString(listOf("50 98 2", "52 50 48"))
    check(testGardenRangeMap.ranges.contains(testGardenRange1))
    check(testGardenRangeMap.ranges.contains(testGardenRange2))
    check(testGardenRangeMap.map(98) == 50L)
    check(testGardenRangeMap.map(53) == 55L)
    check(testGardenRangeMap.map(0) == 0L)

    // test if implementation meets criteria from the description, like:
    val testSolution1 = Day05("Day05_test").part1()
    check(testSolution1 == "35") { "Expected 35, got $testSolution1" }

    val testSolution2 = Day05("Day05_test").part2()
    check(testSolution2 == "46") { "Expected 46, got $testSolution2" }

    solution.run()
}
