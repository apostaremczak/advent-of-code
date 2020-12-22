from solutions.day_22_crab_combat import *


def test_calculate_score():
    assert calculate_score([3, 2, 10, 6, 8, 5, 9, 4, 7, 1]) == 306


def test_play_small_crab():
    player_1, player_2 = read_puzzle_input("puzzle_inputs/day_22.txt")
    winning_deck = [3, 2, 10, 6, 8, 5, 9, 4, 7, 1]
    assert play_small_crab(player_1, player_2) == winning_deck


def test_recursive_combat():
    assert play_recursive_combat(
        [9, 8, 5, 2],
        [10, 1, 7]
    ) == ([5, 10, 2, 9, 8, 7, 1], 2)

    assert play_recursive_combat(
        [8, 1],
        [3, 4, 10, 9, 7, 5]
    ) == ([7, 5, 4, 1, 10, 8, 9, 3], 2)

    assert play_recursive_combat(
        [9, 2, 6, 3, 1],
        [5, 8, 4, 7, 10]
    ) == ([7, 5, 6, 2, 4, 1, 10, 8, 9, 3], 2)


def test_part_2():
    assert part_2([9, 2, 6, 3, 1], [5, 8, 4, 7, 10]) == 291
