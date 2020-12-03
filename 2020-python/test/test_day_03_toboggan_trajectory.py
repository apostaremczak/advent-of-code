from solutions.day_03_toboggan_trajectory import *

test_forest = [
    TreeRow("..##......."),
    TreeRow("#...#...#.."),
    TreeRow(".#....#..#."),
    TreeRow("..#.#...#.#"),
    TreeRow(".#...##..#."),
    TreeRow("..#.##....."),
    TreeRow(".#.#.#....#"),
    TreeRow(".#........#"),
    TreeRow("#.##...#..."),
    TreeRow("#...##....#"),
    TreeRow(".#..#...#.#")
]


def test_counting_at_slope():
    assert count_encountered_trees_on_slope(test_forest, 1, 1) == 2
    assert count_encountered_trees_on_slope(test_forest, 3, 1) == 7
    assert count_encountered_trees_on_slope(test_forest, 5, 1) == 3
    assert count_encountered_trees_on_slope(test_forest, 7, 1) == 4
    assert count_encountered_trees_on_slope(test_forest, 1, 2) == 2


def test_part_1():
    assert part_1(test_forest) == 7


def test_part_2():
    assert part_2(test_forest) == 336
