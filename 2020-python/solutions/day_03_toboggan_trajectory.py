import math
from typing import List


class TreeRow:
    def __init__(self, pattern: str):
        self.pattern = pattern
        self.row_len = len(self.pattern)

    def is_tree_at(self, position: int) -> bool:
        return self.pattern[position % self.row_len] == "#"


def read_puzzle_input(input_file_path: str) -> List[TreeRow]:
    with open(input_file_path, "r") as f:
        return [TreeRow(x.strip()) for x in f.readlines()]


def count_encountered_trees_on_slope(forest: List[TreeRow],
                                     step_right: int,
                                     step_down: int) -> int:
    return sum(
        tree_row.is_tree_at(col_idx * step_right)
        for col_idx, tree_row in enumerate(forest[::step_down])
    )


def part_1(forest: List[TreeRow]) -> int:
    return count_encountered_trees_on_slope(forest, step_down=1, step_right=3)


def part_2(forest: List[TreeRow]) -> int:
    directions = [(1, 1), (3, 1), (5, 1), (7, 1), (1, 2)]
    return math.prod([
        count_encountered_trees_on_slope(forest, step_right, step_down)
        for step_right, step_down in directions
    ])


if __name__ == '__main__':
    trees = read_puzzle_input("../puzzle_inputs/day_03.csv")
    print(f"Solution to part 1: {part_1(trees)}")
    print(f"Solution to part 2: {part_2(trees)}")
