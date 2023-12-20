typealias WorkflowName = String
typealias ParameterName = String

data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    companion object {
        private fun getValue(str: String): Int {
            val v = Regex("\\w=(?<value>\\d+)").find(str)?.groups?.get("value")?.value!!
            return v.toInt()
        }

        /**
         * {x=787,m=2655,a=1222,s=2876}
         */
        fun fromString(str: String): Part {
            val values = str.replace("{", "").replace("}", "").split(",")
            val (x, m, a, s) = values
            return Part(getValue(x), getValue(m), getValue(a), getValue(s))
        }
    }

    fun getSum(): Int =
        x + m + a + s

    fun doesSatisfyRule(rule: String): Boolean {
        val matches = Regex("(?<partName>\\w)(?<comparison>[><])(?<boundaryValue>\\d+)").matchEntire(rule)?.groups
        val partName = matches?.get("partName")?.value!!
        val comparison = matches["comparison"]?.value!!
        val boundaryValue = matches["boundaryValue"]?.value!!.toInt()
        val partValue = when (partName) {
            "x" -> x
            "m" -> m
            "a" -> a
            "s" -> s
            else -> throw Exception("Invalid part name: $partName")
        }
        return when (comparison) {
            ">" -> partValue > boundaryValue
            "<" -> partValue < boundaryValue
            else -> throw Exception("Invalid comparison: $comparison")
        }
    }

    fun isAccepted(workflowMap: Map<WorkflowName, Workflow>, currentWorkflowName: WorkflowName = "in"): Boolean {
        if (currentWorkflowName == "A") {
            return true
        }
        if (currentWorkflowName == "R") {
            return false
        }
        val rules = workflowMap[currentWorkflowName]!!.rules
        rules.forEach { rule ->
            if (rule.rule == null) {
                return isAccepted(workflowMap, rule.output)
            }
            if (doesSatisfyRule(rule.rule)) {
                return isAccepted(workflowMap, rule.output)
            }
        }

        throw Exception("Somehow reached the end of the checking loop")
    }
}

data class Rule(val rule: String?, val output: WorkflowName) {
    companion object {
        fun fromString(strRule: String): Rule {
            val ruleComponents = strRule.split(":")
            return when (ruleComponents.size) {
                1 -> Rule(null, strRule)
                2 -> Rule(ruleComponents.first(), ruleComponents.last())
                else -> throw Exception("Invalid number of elements in $ruleComponents")
            }
        }
    }
}


data class Workflow(val name: WorkflowName, val rules: List<Rule>) {
    companion object {
        /**
         * ex{x>10:one,m<20:two,a>30:R,A}
         * This workflow is named ex and contains four rules. If workflow ex were considering a specific part, it would perform the following steps in order:
         *
         *     Rule "x>10:one": If the part's 'x' is more than 10, send the part to the workflow named one.
         *     Rule "m<20:two": Otherwise, if the part's 'm' is less than 20, send the part to the workflow named two.
         *     Rule "a>30:R": Otherwise, if the part's 'a' is more than 30, the part is immediately rejected (R).
         *     Rule "A": Otherwise, because no other rules matched the part, the part is immediately accepted (A).
         */
        fun fromString(str: String): Workflow {
            val matches = Regex("(?<name>\\w+)\\{(?<rules>.+)\\}").matchEntire(str)?.groups
            val name = matches?.get("name")?.value!!
            val rulesStr = matches["rules"]?.value!!
            val rules = rulesStr.split(",").map { Rule.fromString(it) }
            return Workflow(name, rules)
        }
    }
}

data class SplitParamValueRange(val accepted: ParamValueRange, val rejected: ParamValueRange)

data class ParamValueRange(val start: Int, val end: Int) {
    val length: Long = (end - start + 1).toLong()

    /**
     * If the current range for parameter 'a' is ParamValueRange(1, 1000) and we encounter the condition 'a>100',
     * then we split the range into the rejected ParamValueRange(1, 100) and accepted ParamValueRange(101, 1000);
     * Assumes the condition intersects the range at any point
     */
    fun split(comparison: String, boundaryValue: Int): SplitParamValueRange {
        return if (comparison == "<") {
            SplitParamValueRange(
                accepted = ParamValueRange(start, boundaryValue - 1),
                rejected = ParamValueRange(boundaryValue, end)
            )
        } else {
            SplitParamValueRange(
                accepted = ParamValueRange(boundaryValue + 1, end),
                rejected = ParamValueRange(start, boundaryValue)
            )
        }
    }
}

class Day19 : PuzzleSolutionNonSplitInput {
    override val input: String = readInputNotSplit("Day19")

    fun parseInput(inputStr: String): Pair<Map<WorkflowName, Workflow>, List<Part>> {
        val (workflowsStr, partsStr) = inputStr.split("\n\n")
        val workflows = workflowsStr.split("\n").filter { it.isNotEmpty() }.map { Workflow.fromString(it) }
        val workflowMap: Map<WorkflowName, Workflow> = workflows.associateBy { it.name }
        val parts = partsStr.split("\n").filter { it.isNotEmpty() }.map { Part.fromString(it) }
        return Pair(workflowMap, parts)
    }

    override fun part1(input: String): String {
        val (workflowMap, parts) = parseInput(input)
        val acceptedParts = parts.filter { it.isAccepted(workflowMap) }
        return acceptedParts.sumOf { it.getSum() }.toString()
    }

    private fun countAcceptedCombinations(
        rangesToBeInInvestigated: MutableMap<ParameterName, ParamValueRange>,
        workflowMap: Map<WorkflowName, Workflow>,
        currentWorkflowName: WorkflowName = "in"
    ): Long {
        if (currentWorkflowName == "A") {
            // allowed x range length * allowed m * allowed a * allowed s
            return rangesToBeInInvestigated.values.map { it.length }.reduce(Long::times)
        }
        if (currentWorkflowName == "R") {
            return 0
        }
        var acceptedCombinationCount = 0L
        // Deep copy of the map
        val currentlyInvestigatedRanges = rangesToBeInInvestigated.toList().toMap().toMutableMap()

        val rules = workflowMap[currentWorkflowName]!!.rules
        rules.forEach { rule ->
            if (rule.rule == null) {
                acceptedCombinationCount += countAcceptedCombinations(
                    currentlyInvestigatedRanges,
                    workflowMap,
                    rule.output
                )
                return@forEach
            }

            val matches = Regex("(?<partName>\\w)(?<comparison>[><])(?<boundaryValue>\\d+)")
                .matchEntire(rule.rule)?.groups
            val partName = matches?.get("partName")?.value!!
            val comparison = matches["comparison"]?.value!!
            val boundaryValue = matches["boundaryValue"]?.value!!.toInt()
            val investigatedRange = currentlyInvestigatedRanges[partName]!!

            // Assert the condition intersects the interval
            if (boundaryValue < investigatedRange.start && boundaryValue > investigatedRange.end) {
                throw Exception("Condition $rule doesn't intersect with the range $investigatedRange")
            }
            // Split the interval into multiple branches according to the conditions
            val splitRange = investigatedRange.split(comparison, boundaryValue)

            // Recursively add the length of the accepted subrange
            val acceptedToBeInvestigated = currentlyInvestigatedRanges.toList().toMap().toMutableMap()
            acceptedToBeInvestigated[partName] = splitRange.accepted
            acceptedCombinationCount += countAcceptedCombinations(acceptedToBeInvestigated, workflowMap, rule.output)

            // In the following condition, investigate the rejected values
            currentlyInvestigatedRanges[partName] = splitRange.rejected
        }

        return acceptedCombinationCount
    }

    override fun part2(input: String): String {
        val (workflowMap, _) = parseInput(input)
        val startingRanges = mutableMapOf(
            "x" to ParamValueRange(1, 4000),
            "m" to ParamValueRange(1, 4000),
            "a" to ParamValueRange(1, 4000),
            "s" to ParamValueRange(1, 4000)
        )
        return countAcceptedCombinations(startingRanges, workflowMap).toString()
    }

}

fun main() {
    // Test parsing
    val testPart1 = Part.fromString("{x=787,m=2655,a=1222,s=2876}")
    check(testPart1 == Part(787, 2655, 1222, 2876))
    check(!testPart1.doesSatisfyRule("s<1351"))
    check(testPart1.doesSatisfyRule("m>1548"))

    val testRule1 = Rule.fromString("x>10:one")
    check(testRule1 == Rule("x>10", "one"))
    check(Rule.fromString("R") == Rule(null, "R"))

    val testWorkflow1 = Workflow.fromString("ex{x>10:one,m<20:two,A}")
    val expectedWorkflow1 = Workflow(
        "ex", listOf(
            Rule("x>10", "one"),
            Rule("m<20", "two"),
            Rule(null, "A")
        )
    )
    check(testWorkflow1 == expectedWorkflow1) { "Expected $expectedWorkflow1, got $testWorkflow1" }

    val solution = Day19()

    val testInput = solution.readInputNotSplit("Day19_test")

    // Test accepting parts
    val (testWorkflowMap, _) = solution.parseInput(testInput)
    check(testPart1.isAccepted(testWorkflowMap))
    val testPart2 = Part.fromString("{x=1679,m=44,a=2067,s=496}")
    check(!testPart2.isAccepted(testWorkflowMap))

    // test if implementation meets criteria from the description, like:
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "19114") { "Expected 19114, got $testSolution1" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "167409079868000") { "Expected 167409079868000, got $testSolution2" }

    solution.run()
}
