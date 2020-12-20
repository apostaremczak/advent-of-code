from solutions.day_20_jurassic_jigsaw import *

test_input = read_puzzle_input("puzzle_inputs/day_20.txt")


def test_find_edgy_tiles():
    edgy_tiles = set(t.id for t in find_edgy_tiles(test_input))
    expected_edges = {1951, 3079, 2971, 1171}
    assert set(edgy_tiles) == expected_edges


def test_part_1():
    assert part_1(test_input) == 20899048083289
