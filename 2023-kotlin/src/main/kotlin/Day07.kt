// Replace the original card names to use lexicographic ordering for comparisons
val AlphabetReversed = 'Z'.downTo('A').toList()

enum class HandType : Comparable<HandType> {
    HighCard {
        override val distribution: List<Int> = listOf(1, 1, 1, 1, 1)
    },

    OnePair {
        override val distribution: List<Int> = listOf(2, 1, 1, 1)
    },

    TwoPair {
        override val distribution: List<Int> = listOf(2, 2, 1)
    },

    ThreeOfAKind {
        override val distribution: List<Int> = listOf(3, 1, 1)
    },

    FullHouse {
        override val distribution: List<Int> = listOf(3, 2)
    },

    FourOfAKind {
        override val distribution: List<Int> = listOf(4, 1)
    },

    FiveOfAKind {
        override val distribution: List<Int> = listOf(5)
    };

    abstract val distribution: List<Int>
}

abstract class GenericHand : Comparable<GenericHand> {
    abstract val cards: String
    abstract val originalCards: String
    abstract val bid: Int
    abstract val handType: HandType

    override fun compareTo(other: GenericHand): Int {
        return when {
            handType > other.handType -> 1
            handType < other.handType -> -1
            else -> cards.compareTo(other.cards)
        }
    }

    protected fun getDistribution(currentCards: String): List<Int> {
        return currentCards.toSet().map { letter -> currentCards.count { it == letter } }.sortedDescending()
    }

    protected fun getDistribution(): List<Int> {
        return getDistribution(cards)
    }

    companion object {
        fun fromString(line: String, originalCardsOrdering: List<Char>): Triple<String, String, Int> {
            val replacedOrdering = AlphabetReversed.subList(0, originalCardsOrdering.size)
            val cardMapping = originalCardsOrdering.zip(replacedOrdering)
                    .associate { (original, replaced) -> original to replaced }
            val matches = Regex("(?<hand>[A-Z0-9]+) (?<bid>[0-9]+)").matchEntire(line)?.groups
            val originalHand = matches?.get("hand")?.value!!
            val replacedHand = originalHand.map { cardMapping[it] }
            val hand = replacedHand.joinToString("")
            val bid = matches["bid"]?.value!!.toInt()
            return Triple(hand, originalHand, bid)
        }
    }
}


data class Hand(override val cards: String, override val originalCards: String, override val bid: Int) : GenericHand() {
    override val handType: HandType
        get() = HandType.entries.first { it.distribution == getDistribution() }

    companion object {
        fun fromString(line: String): Hand {
            val originalCardsOrdering = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
            val args = fromString(line, originalCardsOrdering)
            return Hand(args.first, args.second, args.third)
        }
    }
}

data class JokerHand(override val cards: String, override val originalCards: String, override val bid: Int) : GenericHand() {
//    override val handType: HandType
//        get() {
//            var jokerCount = originalCards.count { it == 'J' }
//            val nonJokerDistribution = getDistribution(originalCards.filter { it != 'J' }).toMutableList()
//            val distribution = getDistribution(cards).toMutableList()
//
//            while (jokerCount > 0) {
//                val lastIdx = distribution.size - 1
//                distribution[0] += 1
//                distribution[lastIdx] -= 1
//                if (distribution[lastIdx] == 0) {
//                    distribution.removeAt(lastIdx)
//                }
//                jokerCount -= 1
//            }
//
//            return HandType.entries.first { it.distribution == distribution }
//        }

    override val handType: HandType
        get() {
            val jokerCount = originalCards.count { it == 'J' }
            val nonJokerDistribution = getDistribution(originalCards.filter { it != 'J' }).toMutableList()
            if (nonJokerDistribution.isEmpty()) {
                // Got jokers only
                return HandType.FiveOfAKind
            }
            nonJokerDistribution[0] += jokerCount
            return HandType.entries.first { it.distribution == nonJokerDistribution }
        }

    companion object {
        fun fromString(line: String): JokerHand {
            val originalCardsOrdering = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')
            val args = fromString(line, originalCardsOrdering)
            return JokerHand(args.first, args.second, args.third)
        }
    }
}

class Day07 : PuzzleSolution {
    override val input: List<String> = readInput("Day07")
    override fun part1(input: List<String>): String {
        val hands = input.map { Hand.fromString(it) }.sorted()
        val points = hands.mapIndexed { index, hand ->
            val score = hand.bid * (index + 1)
            score
        }
        return points.sum().toString()
    }

    override fun part2(input: List<String>): String {
        val hands = input.map { JokerHand.fromString(it) }.sorted()
        val points = hands.mapIndexed { index, hand ->
            val score = hand.bid * (index + 1)
            score
        }
        return points.sum().toString()
    }
    // 248046462 is too high
}

fun main() {
    val solution = Day07()

    // Test hand type ordering
    val expectedOrdering = listOf(
            HandType.HighCard, HandType.OnePair, HandType.TwoPair, HandType.ThreeOfAKind,
            HandType.FullHouse, HandType.FourOfAKind, HandType.FiveOfAKind
    )
    check(HandType.entries.toList() == expectedOrdering)

    // Test hand parsing
    val testHand1 = Hand.fromString("32T3K 765")
    check(testHand1 == Hand("ONVOY", "32T3K", 765))
    val testHand2 = Hand.fromString("KTJJT 220")
    val testHand3 = Hand.fromString("KK677 28")
    val testHand4 = Hand.fromString("T55J5 684")

    // Test hand ordering
    check(testHand1 < testHand3)
    check(testHand1 < testHand2)
    check(testHand2 < testHand3)
    check(testHand2 < testHand4)
    check(testHand3 < testHand4)

    // Test joker hand parsing
    val jokerTestHand1 = JokerHand.fromString("T55J5 684")
    val jokerTestHand2 = JokerHand.fromString("KTJJT 220")
    check(jokerTestHand1 < jokerTestHand2)
    val jokerTestHand3 = JokerHand.fromString("JJJJJ 550")
    check(jokerTestHand3.handType == HandType.FiveOfAKind)
    val jokerTestHand4 = JokerHand.fromString("8JJJJ 769")
    check(jokerTestHand4.handType == HandType.FiveOfAKind)
    val jokerTestHand5 = JokerHand.fromString("KJJJ6 451")
    check(jokerTestHand5.handType == HandType.FourOfAKind) { "Expected four of a kind, got ${jokerTestHand5.handType}" }


    // test if implementation meets criteria from the description, like:
    val testInput = solution.readInput("Day07_test")
    val testSolution1 = solution.part1(testInput)
    check(testSolution1 == "6440") { "Expected 6440, got $testSolution1" }

    val testSolution2 = solution.part2(testInput)
    check(testSolution2 == "5905") { "Expected 5905, got $testSolution2" }

    solution.run()
}
