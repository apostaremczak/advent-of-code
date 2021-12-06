from solutions.day_21_allergen_assessment import *

food_list = read_puzzle_input("puzzle_inputs/day_21.txt")


def test_part_1():
    assert part_1(food_list) == 5


def test_part_2():
    assert part_2(food_list) == "mxmxvkd,sqjhc,fvjkl"
