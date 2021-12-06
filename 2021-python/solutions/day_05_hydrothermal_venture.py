import re
from collections import Counter
from dataclasses import dataclass
from typing import List, Tuple


@dataclass
class Point:
    x: int
    y: int

    def connect(self, other, allow_diagonal) -> list:
        if self.x >= other.x:
            x_range = range(other.x, self.x + 1)
        else:
            x_range = range(other.x, self.x - 1, -1)

        if self.y >= other.y:
            y_range = range(other.y, self.y + 1)
        else:
            y_range = range(other.y, self.y - 1, -1)

        if self.x == other.x:
            return [Point(self.x, y) for y in y_range]
        if self.y == other.y:
            return [Point(x, self.y) for x in x_range]
        if allow_diagonal:
            return [Point(x, y) for x, y in zip(x_range, y_range)]
        return []

    def __hash__(self):
        return hash((self.x, self.y))


def read_input(file_path: str) -> List[Tuple[Point, Point]]:
    points = []
    with open(file_path, "r") as f:
        for line in f.readlines():
            coords = re.match(r"(\d+),(\d+) -> (\d+),(\d+)", line).groups()
            x1, y1, x2, y2 = coords
            points.append((Point(int(x1), int(y1)), Point(int(x2), int(y2))))
    return points


def count_intersections(entries: List[Tuple[Point, Point]],
                        allow_intersections: bool) -> int:
    segment_counts = Counter()
    for point1, point2 in entries:
        segment = point1.connect(point2, allow_intersections)
        for p in segment:
            segment_counts[p] += 1
    return sum([c >= 2 for c in segment_counts.values()])


def part_1(entries: List[Tuple[Point, Point]]) -> int:
    return count_intersections(entries, allow_intersections=False)


def part_2(entries: List[Tuple[Point, Point]]) -> int:
    return count_intersections(entries, allow_intersections=True)


if __name__ == '__main__':
    puzzle_input = read_input("../puzzle_inputs/day_05.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
