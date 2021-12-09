from math import prod
from dataclasses import dataclass
from typing import Dict, List, Tuple

Coord = Tuple[int, int]


@dataclass
class Point:
    coord: Coord
    value: int
    adjacent_points: List[Coord]


def read_puzzle_input(input_file_path: str) -> List[List[int]]:
    with open(input_file_path, "r") as f:
        return [[int(x) for x in line.strip()] for line in f.readlines()]


def create_heightmap(raw_height_map: List[List[int]]) -> Dict[Coord, Point]:
    heightmap = {}
    max_row = len(raw_height_map) - 1
    max_col = len(raw_height_map[0]) - 1

    for x, row in enumerate(raw_height_map):
        for y, value in enumerate(row):
            adjacent = []
            if x != 0:
                adjacent.append((x - 1, y))
            if x != max_row:
                adjacent.append((x + 1, y))
            if y != 0:
                adjacent.append((x, y - 1))
            if y != max_col:
                adjacent.append((x, y + 1))
            heightmap[(x, y)] = Point((x, y), value, adjacent)
    return heightmap


def find_low_points(heightmap: Dict[Coord, Point]) -> List[Point]:
    return [
        point
        for point in heightmap.values()
        if point.value < min([
            heightmap[adj].value
            for adj in point.adjacent_points
        ])
    ]


def part_1(entries: List[List[int]]) -> int:
    low_points = find_low_points(create_heightmap(entries))
    return sum(point.value + 1 for point in low_points)


def find_basin(low_point: Point, heightmap: Dict[Coord, Point]) -> List[Point]:
    queue = [low_point]
    visited = [low_point]

    while queue:
        point = queue.pop(0)
        for x, y in point.adjacent_points:
            neighbor = heightmap[(x, y)]
            if neighbor not in visited and neighbor.value != 9:
                queue.append(neighbor)
                visited.append(neighbor)
    return visited


def part_2(entries: List[List[int]]) -> int:
    heightmap = create_heightmap(entries)
    low_points = find_low_points(heightmap)

    basin_sizes = [
        len(find_basin(low_point, heightmap))
        for low_point in low_points
    ]

    return prod(sorted(basin_sizes)[-3:])


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_09.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
