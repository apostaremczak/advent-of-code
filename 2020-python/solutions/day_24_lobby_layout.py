import numpy as np
import re
from dataclasses import dataclass
from enum import Enum
from typing import List, Dict


class Direction(Enum):
    E = "e"
    SE = "se"
    SW = "sw"
    W = "w"
    NW = "nw"
    NE = "ne"


class Color(Enum):
    WHITE = 0
    BLACK = 1


@dataclass
class HexagonTile:
    coordinates: np.ndarray

    def navigate(self, direction: Direction):
        if direction == Direction.E:
            direction_vector = np.array([1, 0])
        elif direction == Direction.W:
            direction_vector = np.array([-1, 0])
        else:
            if direction == Direction.NE:
                direction_vector = np.array([1, -1])
            elif direction == Direction.NW:
                direction_vector = np.array([0, -1])
            elif direction == Direction.SW:
                direction_vector = np.array([0, 1])
            elif direction == Direction.SE:
                direction_vector = np.array([1, 1])

            # Offset for even columns
            if not self.coordinates[1] % 2:
                direction_vector += np.array([-1, 0])

        return HexagonTile(self.coordinates + direction_vector)

    def get_neighbors(self):
        return [self.navigate(d) for d in Direction]

    def __hash__(self):
        return hash(tuple(self.coordinates))

    def __eq__(self, other):
        return np.array_equal(self.coordinates, other.coordinates)


def parse_directions(directions_str: str) -> List[Direction]:
    tile_dir = []
    while directions_str:
        direction = re.match(r"^(e|se|sw|w|nw|ne)", directions_str).group(0)
        tile_dir.append(Direction(direction))
        directions_str = directions_str[len(direction):]
    return tile_dir


def read_puzzle_input(input_file_path: str) -> List[List[Direction]]:
    tile_directions = []
    with open(input_file_path, "r") as f:
        for tile_line in f.read().splitlines():
            tile_directions.append(parse_directions(tile_line))
    return tile_directions


def arrange_tiles(flipping_directions: List[List[Direction]]) -> Dict[HexagonTile, Color]:
    tiles: Dict[HexagonTile, Color] = {}
    reference_tile = HexagonTile(coordinates=np.array([0, 0]))

    for directions in flipping_directions:
        current_tile = reference_tile
        for direction in directions:
            current_tile = current_tile.navigate(direction)

        # Decide on the color of this final tile
        if current_tile in tiles:
            current_tile_col = Color((tiles[current_tile].value + 1) % 2)
        else:
            current_tile_col = Color.BLACK

        # Note the color
        tiles[current_tile] = current_tile_col
    return tiles


def part_1(entries: List[List[Direction]]) -> int:
    tiles = arrange_tiles(entries)
    return sum([t.value for t in tiles.values()])


def simulate_single_day(tile_arrangement: Dict[HexagonTile, Color]) -> Dict[HexagonTile, Color]:
    new_state = {}
    for tile, color in tile_arrangement.items():
        neighbors = tile.get_neighbors()
        adjacent_sum = 0
        for neighbor in neighbors:
            if neighbor in tile_arrangement:
                adjacent_sum += tile_arrangement[neighbor].value
            else:
                new_state[neighbor] = Color.WHITE

        if color == Color.WHITE and adjacent_sum == 2:
            new_color = Color.BLACK
        elif (color == Color.BLACK) and (adjacent_sum == 0 or adjacent_sum > 2):
            new_color = Color.WHITE
        else:
            new_color = color
        new_state[tile] = new_color
    return new_state


def part_2(entries: List[List[Direction]], num_days: int = 100) -> int:
    arrangement = arrange_tiles(entries)

    # Fill in blank spaces with white tiles
    tile_coords = np.array([tile.coordinates for tile in arrangement.keys()])
    xs = tile_coords[:, 0]
    ys = tile_coords[:, 1]

    for x in range(min(xs) - 1, max(xs) + 2):
        for y in range(min(ys) - 1, max(ys) + 2):
            tile = HexagonTile(np.array([x, y]))
            if tile not in arrangement:
                arrangement[tile] = Color.WHITE

    for i in range(num_days):
        print(f"Simulating day {i}, current arrangement size: {len(arrangement)}")
        arrangement = simulate_single_day(arrangement)

    return sum([t.value for t in arrangement.values()])


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_24.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
