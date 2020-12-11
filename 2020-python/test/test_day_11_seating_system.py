from solutions.day_11_seating_system import *


def test_part_1():
    starting_arrangement = read_puzzle_input("puzzle_inputs/day_11.txt")
    assert part_1(starting_arrangement) == 37


def test_part_2():
    starting_arrangement = read_puzzle_input("puzzle_inputs/day_11.txt")
    assert part_2(starting_arrangement) == 26
