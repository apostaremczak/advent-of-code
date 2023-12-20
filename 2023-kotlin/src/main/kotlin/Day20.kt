const val Broadcaster = "broadcaster"

enum class PulseType {
    LOW, HIGH
}

data class Pulse(val sourceModule: String?, val module: String, val pulseType: PulseType)

data class MachineStates(
        val modules: Map<String, List<String>>,
        var lowPulseCount: Long = 0,
        var highPulseCount: Long = 0
) {
    private val modulesNoPrefixes: Map<String, List<String>> = modules.map { (key, dest) ->
        key.replace("%", "").replace("&", "") to dest
    }.toMap()

    // Flip-flop modules (prefix %) are either on or off; they are initially off.
    private val flipModules = modules.filterKeys { it.startsWith("%") }.keys
            .map { it.substringAfter("%") }.toSet()

    // Conjunction modules (prefix &) remember the type of the most recent pulse received from each input
    private val conjunctionModules = modules.filterKeys { it.startsWith("&") }.keys
            .map { it.substringAfter("&") }.toSet()

    // Find all source modules that lead to conjunction modules
    private val conjunctionInputModules: Map<String, Set<String>> = conjunctionModules
            .associateWith { conjunctionModuleName ->
                modulesNoPrefixes.filterValues { it.contains(conjunctionModuleName) }.keys
            }

    // Flip-flop modules (prefix %) are initially off.
    private val turnedOnModules = mutableSetOf<String>()

    // Map: { conjunction module -> Map: {input module -> last known pulse type} }
    private val conjunctionMemory: MutableMap<String, MutableMap<String, PulseType>> = conjunctionInputModules
            .map { (conjunctionModuleName, inputs) ->
                conjunctionModuleName to inputs.associateWith { PulseType.LOW }.toMutableMap()
            }.toMap().toMutableMap()

    companion object {
        fun parseInput(input: List<String>): MachineStates {
            val modules: Map<String, List<String>> = input.associate { line ->
                val module = line.substringBefore(" ->")
                val destinations = line.substringAfter("-> ").split(", ")
                module to destinations
            }
            return MachineStates(modules)
        }
    }

    fun getPulseProduct(): Long = lowPulseCount * highPulseCount

    fun pushButton(searchForPulse: Pulse? = null): Boolean {
        val queue = ArrayDeque<Pulse>()
        // When you push the button, a single low pulse is sent directly to the broadcaster module.
        queue.addLast(Pulse(null, Broadcaster, PulseType.LOW))
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (searchForPulse != null) {
                if (current.sourceModule == searchForPulse.sourceModule
                        && current.module == searchForPulse.module
                        && current.pulseType == searchForPulse.pulseType) {
                    return true
                }
            }

            when (current.pulseType) {
                PulseType.LOW -> lowPulseCount++
                PulseType.HIGH -> highPulseCount++
            }
            val destinations = modulesNoPrefixes[current.module] ?: emptyList()

            // There is a single broadcast module (named broadcaster).
            // When it receives a pulse, it sends the same pulse to all of its destination modules.
            if (current.module == Broadcaster) {
                queue.addAll(destinations.map { Pulse(current.module, it, current.pulseType) })
            }

            // Flip-flop modules (prefix %) are either on or off; they are initially off.
            // If a flip-flop module receives a high pulse, it is ignored and nothing happens.
            // However, if a flip-flop module receives a low pulse, it flips between on and off.
            // If it was off, it turns on and sends a high pulse.
            // If it was on, it turns off and sends a low pulse.
            if (flipModules.contains(current.module)) {
                if (current.pulseType == PulseType.LOW) {
                    if (turnedOnModules.contains(current.module)) {
                        // Module is turned on
                        turnedOnModules.remove(current.module) // Turn it off
                        queue.addAll(destinations.map { Pulse(current.module, it, PulseType.LOW) })
                    } else {
                        // Module is turned off
                        turnedOnModules.add(current.module) // Turn it on
                        queue.addAll(destinations.map { Pulse(current.module, it, PulseType.HIGH) })
                    }
                }
            }

            // Conjunction modules (prefix &) remember the type of the most recent pulse received from each of
            // their connected input modules; they initially default to remembering a low pulse for each input.
            // When a pulse is received, the conjunction module first updates its memory for that input.
            // Then, if it remembers high pulses for all inputs, it sends a low pulse;
            // otherwise, it sends a high pulse.
            if (conjunctionModules.contains(current.module)) {
                val memory: MutableMap<String, PulseType> = conjunctionMemory[current.module]!!
                if (current.sourceModule != null) {
                    memory[current.sourceModule] = current.pulseType
                }
                conjunctionMemory[current.module] = memory
                if (memory.all { it.value == PulseType.HIGH }) {
                    queue.addAll(destinations.map { Pulse(current.module, it, PulseType.LOW) })
                } else {
                    queue.addAll(destinations.map { Pulse(current.module, it, PulseType.HIGH) })
                }
            }
        }

        return false
    }

    fun pushButtonTimes(pushTimes: Int) {
        (1..pushTimes).forEach { _ ->
            pushButton()
        }
    }
}


class Day20 : PuzzleSolution {
    override val input: List<String> = readInput("Day20")

    override fun part1(input: List<String>): String {
        val machineStates = MachineStates.parseInput(input)
        // Push the button 1000 times
        machineStates.pushButtonTimes(1000)
        return machineStates.getPulseProduct().toString()
    }

    override fun part2(input: List<String>): String {
        // Waiting for all pulses to be fully handled after each button press,
        // what is the fewest number of button presses required to deliver a single low pulse to the module named rx?

        // In my case, the rx module is turned on by a single qt conjunction module, which needs to send a LOW pulse
        // The sources for qt are conjunction modules mr, kk, gl and bb, each needing a HIGH pulse
        // Each one of them also has a single input conjunction module, each needing a LOW pulse
        // So if we find the number of button presses need to send a LOW pulse to each of those modules
        // and calculate their LCM, we can determine the fewest presses needed
        val searchedPulses = listOf(
                Pulse("nl", "mr", PulseType.LOW),
                Pulse("cr", "kk", PulseType.LOW),
                Pulse("jx", "gl", PulseType.LOW),
                Pulse("vj", "bb", PulseType.LOW)
        )

        val cycleLengths: List<Long> = searchedPulses.map { pulse ->
            // Find number of button presses to deliver pulse
            val machineStates = MachineStates.parseInput(input)
            (1..5000L).firstNotNullOf { buttonPressIdx ->
                val wasFound = machineStates.pushButton(searchForPulse = pulse)
                when (wasFound) {
                    true -> buttonPressIdx
                    false -> null
                }
            }
        }
        return cycleLengths.lcm().toString()
    }
}

fun main() {
    val solution = Day20()

    // test if implementation meets criteria from the description, like:
    val testInput1 = solution.readInput("Day20_test1")

    val machineStates1 = MachineStates.parseInput(testInput1)
    machineStates1.pushButtonTimes(1)
    check(machineStates1.lowPulseCount == 8L) { "Expected low pulse count of 8, got ${machineStates1.lowPulseCount}" }
    check(machineStates1.highPulseCount == 4L) { "Expected high pulse count of 4, got ${machineStates1.highPulseCount}" }

    val testSolution1 = solution.part1(testInput1)
    check(testSolution1 == "32000000") { "Expected 32000000, got $testSolution1" }

    val testInput2 = solution.readInput("Day20_test2")
    val testSolution2 = solution.part1(testInput2)
    check(testSolution2 == "11687500") { "Expected 11687500, got $testSolution1" }

    solution.run()
}
