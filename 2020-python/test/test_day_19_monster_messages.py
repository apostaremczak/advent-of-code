from solutions.day_19_monster_messages import *


def test_part_1():
    test_input = read_puzzle_input("puzzle_inputs/day_19.txt")
    assert part_1(*test_input) == 2


def test_part_2():
    test_input = read_puzzle_input("puzzle_inputs/day_19_2.txt")
    assert part_2(*test_input) == 12
