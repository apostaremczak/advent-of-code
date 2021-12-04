import numpy as np
import re

from collections import OrderedDict
from typing import List, Tuple

Instruction = Tuple[str, int]


class Submarine:
    DIRECTIONS = OrderedDict([
        ("up", np.array([0, -1])),
        ("forward", np.array([1, 0])),
        ("down", np.array([0, 1]))
    ])

    def __init__(self):
        self.position = np.array([0, 0])
        self.aim = np.array([0, 0])

    def move(self, instruction: Instruction):
        direction, value = instruction
        self.position += value * Submarine.DIRECTIONS[direction]


class SubmarineWithAim:
    def __init__(self):
        self.depth = 0
        self.horizontal = 0
        self.aim = 0

    def move(self, instruction: Instruction):
        direction, value = instruction
        if direction == "down":
            self.aim += value
        elif direction == "up":
            self.aim -= value
        elif direction == "forward":
            self.horizontal += value
            self.depth += (self.aim * value)


def read_puzzle_input(input_file_path: str) -> List[Instruction]:
    instructions = []
    with open(input_file_path, "r") as f:
        for line in f.readlines():
            matches = re.match(r"(\w+)\s(\d+)", line)
            action, value = matches.groups()
            instructions.append((action, int(value)))
    return instructions


def part_1(instructions: List[Instruction]) -> int:
    submarine = Submarine()
    for instruction in instructions:
        submarine.move(instruction)
    return submarine.position[0] * submarine.position[1]


def part_2(instructions: List[Instruction]) -> int:
    submarine = SubmarineWithAim()
    for instruction in instructions:
        submarine.move(instruction)
    return submarine.depth * submarine.horizontal


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_02.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
