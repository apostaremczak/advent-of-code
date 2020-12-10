from solutions.day_10_adapter_array import *

joltage_input_1 = [16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4]

joltage_input_2 = [28, 33, 18, 42, 31, 14, 46, 20, 48, 47, 24, 23, 49,
                   45, 19, 38, 39, 11, 1, 32, 25, 35, 8, 17, 7, 9, 4, 2,
                   34, 10, 3]


def test_part_1():
    assert part_1(joltage_input_1) == 7 * 5
    assert part_1(joltage_input_2) == 22 * 10


def test_part_2():
    assert part_2(joltage_input_1) == 8
    assert part_2(joltage_input_2) == 19208
