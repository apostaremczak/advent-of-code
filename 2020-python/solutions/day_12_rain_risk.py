import numpy as np
import re

from collections import OrderedDict
from typing import List, Tuple

Instruction = Tuple[str, int]

DIRECTIONS = OrderedDict([
    ("N", np.array([-1, 0])),
    ("E", np.array([0, 1])),
    ("S", np.array([1, 0])),
    ("W", np.array([0, -1]))
])


def read_puzzle_input(input_file_path: str) -> List[Instruction]:
    instructions = []
    with open(input_file_path, "r") as f:
        for line in f.readlines():
            matches = re.match(r"(\w)(\d+)", line)
            action, value = matches.groups()
            instructions.append((action, int(value)))
    return instructions


class LonelyShip:
    DIRECTION_NAMES = list(DIRECTIONS.keys())

    def __init__(self):
        self.position = np.array([0, 0])
        self.facing = "E"

    def move_forward(self, value):
        self.position += value * DIRECTIONS[self.facing]

    def move_in_direction(self, direction, value):
        self.position += value * DIRECTIONS[direction]

    def rotate_ship(self, direction, value):
        if direction == "L":
            self.rotate_ship("R", 360 - value)
        else:
            curr_idx = LonelyShip.DIRECTION_NAMES.index(self.facing)
            new_idx = (curr_idx + value // 90) % 4
            self.facing = LonelyShip.DIRECTION_NAMES[new_idx]

    def apply_instruction(self, instruction: Instruction):
        direction, value = instruction
        if direction == "F":
            self.move_forward(value)
        elif direction in DIRECTIONS:
            self.move_in_direction(direction, value)
        else:
            self.rotate_ship(direction, value)


def part_1(instructions: List[Instruction]) -> int:
    ship = LonelyShip()
    for instruction in instructions:
        ship.apply_instruction(instruction)
    return np.sum(np.abs(ship.position))


class Ship:
    ROTATE_LEFT = np.array([[0, -1], [1, 0]])
    ROTATE_RIGHT = np.array([[0, 1], [-1, 0]])

    def __init__(self):
        self.position = np.array([0, 0])
        self.waypoint = np.array([-1, 10])

    def move_waypoint(self, direction, value):
        dir_vec = DIRECTIONS[direction]
        self.waypoint += value * dir_vec

    def rotate_waypoint(self, rotation_matrix, value):
        rotation = np.linalg.matrix_power(rotation_matrix, value // 90)
        self.waypoint = rotation @ self.waypoint

    def move_forward(self, value):
        self.position += value * self.waypoint

    def apply_instruction(self, instruction: Instruction):
        direction, value = instruction
        if direction in DIRECTIONS:
            self.move_waypoint(direction, value)
        elif direction == "F":
            self.move_forward(value)
        elif direction == "R":
            self.rotate_waypoint(Ship.ROTATE_RIGHT, value)
        else:
            self.rotate_waypoint(Ship.ROTATE_LEFT, value)


def part_2(instructions: List[Instruction]) -> int:
    ship = Ship()
    for instruction in instructions:
        ship.apply_instruction(instruction)
    return np.sum(np.abs(ship.position))


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_12.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
