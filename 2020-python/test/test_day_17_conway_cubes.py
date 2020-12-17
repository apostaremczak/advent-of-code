from solutions.day_17_conway_cubes import *

test_input = read_puzzle_input("puzzle_inputs/day_17.txt")


def test_get_neighbors():
    cube = (1, 2, 3)
    neighbors = get_neighbors(cube)
    assert len(neighbors) == 26
    assert (2, 2, 2) in neighbors
    assert (0, 2, 3) in neighbors

    cube_4d = (1, 2, 3, 4)
    neighbors_4d = get_neighbors(cube_4d)
    assert len(neighbors_4d) == 80
    assert (2, 2, 3, 3) in neighbors_4d
    assert (0, 2, 3, 4) in neighbors_4d


def test_part_1():
    assert part_1(test_input) == 112


def test_part_2():
    assert part_2(test_input) == 848
