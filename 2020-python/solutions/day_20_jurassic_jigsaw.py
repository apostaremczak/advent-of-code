import re
from copy import deepcopy
from itertools import chain
from math import prod
from typing import List


class Tile:
    def __init__(self, representation: str):
        tile_lines = representation.split("\n")
        self.id = int(re.findall(r"^Tile (\d+):$", tile_lines[0])[0])
        self.content = tile_lines[1:]
        self.borders = self._get_borders()
        self.borders_flipped = [b[::-1] for b in self.borders]
        self.shared_borders = 0

    def _get_borders(self):
        top = self.content[0]
        bottom = self.content[-1]
        left = "".join([row[0] for row in self.content])
        right = "".join([row[-1] for row in self.content])
        return [top, right, bottom, left]

    def flip(self):
        pass

    def rotate(self, n_times: int):
        pass


def read_puzzle_input(input_file_path: str) -> List[Tile]:
    with open(input_file_path, "r") as f:
        return [Tile(x.strip()) for x in f.read().split("\n\n") if x]


def find_edgy_tiles(tiles_: List[Tile]) -> List[Tile]:
    tiles = [deepcopy(t) for t in tiles_]
    all_borders = {
        tile.id: tile.borders
        for tile in tiles
    }

    for tile in tiles:
        other_borders = set(
            chain.from_iterable([
                b for t, b in all_borders.items() if t != tile.id
            ])
        )
        for border in tile.borders + tile.borders_flipped:
            if border in other_borders:
                tile.shared_borders += 1

    edge_tiles = [t for t in tiles if t.shared_borders == 2]
    return edge_tiles


def part_1(tiles: List[Tile]) -> int:
    return prod(t.id for t in find_edgy_tiles(tiles))


def part_2(tiles: List[Tile]) -> int:
    pass


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_20.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
