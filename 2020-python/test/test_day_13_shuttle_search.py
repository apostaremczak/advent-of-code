from solutions.day_13_shuttle_search import *

schedule = read_puzzle_input("puzzle_inputs/day_13.txt")


def test_calculate_wait_time():
    assert calculate_wait_time(939, 59) == 5
    assert calculate_wait_time(939, 7) == 6


def test_part_1():
    assert part_1(*schedule) == 295


def test_part_2():
    assert part_2(schedule[1]) == 1068781
    assert part_2([17, None, 13, 19]) == 3417
