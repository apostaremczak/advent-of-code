import numpy as np
from solutions.day_24_lobby_layout import *


def test_navigating_hexagon_tiles():
    # Test even columns
    tile = HexagonTile(np.array([2, 2]))
    assert tile.navigate(Direction.E) == HexagonTile(np.array([3, 2]))
    assert tile.navigate(Direction.SE) == HexagonTile(np.array([2, 3]))
    assert tile.navigate(Direction.SW) == HexagonTile(np.array([1, 3]))
    assert tile.navigate(Direction.W) == HexagonTile(np.array([1, 2]))
    assert tile.navigate(Direction.NW) == HexagonTile(np.array([1, 1]))
    assert tile.navigate(Direction.NE) == HexagonTile(np.array([2, 1]))

    # Test odd columns
    tile = HexagonTile(np.array([3, 1]))
    assert tile.navigate(Direction.E) == HexagonTile(np.array([4, 1]))
    assert tile.navigate(Direction.SE) == HexagonTile(np.array([4, 2]))
    assert tile.navigate(Direction.SW) == HexagonTile(np.array([3, 2]))
    assert tile.navigate(Direction.W) == HexagonTile(np.array([2, 1]))
    assert tile.navigate(Direction.NW) == HexagonTile(np.array([3, 0]))
    assert tile.navigate(Direction.NE) == HexagonTile(np.array([4, 0]))


def test_getting_tile_neighbors():
    tile = HexagonTile(np.array([2, 2]))
    expected_neighbors = [
        HexagonTile(np.array([3, 2])),
        HexagonTile(np.array([2, 3])),
        HexagonTile(np.array([1, 3])),
        HexagonTile(np.array([1, 2])),
        HexagonTile(np.array([1, 1])),
        HexagonTile(np.array([2, 1]))
    ]
    assert tile.get_neighbors() == expected_neighbors


def test_part_1():
    directions = read_puzzle_input("puzzle_inputs/day_24.txt")
    assert part_1(directions) == 10


def test_part_2():
    directions = read_puzzle_input("puzzle_inputs/day_24.txt")
    assert part_2(directions, num_days=1) == 15
    assert part_2(directions, num_days=2) == 12
    assert part_2(directions, num_days=3) == 25
    assert part_2(directions, num_days=10) == 37
    assert part_2(directions, num_days=20) == 132
    assert part_2(directions, num_days=100) == 2208
