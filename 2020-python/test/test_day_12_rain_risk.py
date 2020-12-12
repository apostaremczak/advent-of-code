from solutions.day_12_rain_risk import *


def test_part_1():
    instructions = read_puzzle_input("puzzle_inputs/day_12.txt")
    assert part_1(instructions) == 25


def test_part_2():
    instructions = read_puzzle_input("puzzle_inputs/day_12.txt")
    assert part_2(instructions) == 286
