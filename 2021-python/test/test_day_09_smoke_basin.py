from solutions.day_09_smoke_basin import *

test_input = read_puzzle_input("puzzle_inputs/day_09.txt")


def test_part_1():
    assert part_1(test_input) == 15


def test_find_basin():
    heightmap = create_heightmap(test_input)

    p_1 = heightmap[(0, 1)]
    assert len(find_basin(p_1, heightmap)) == 3

    p_2 = heightmap[(0, 9)]
    assert len(find_basin(p_2, heightmap)) == 9


def test_part_2():
    assert part_2(test_input) == 1134
