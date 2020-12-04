import re
from dataclasses import dataclass
from typing import Callable, List


@dataclass
class PasswordRule:
    min_count: int
    max_count: int
    letter: str

    def is_count_valid(self, password: str) -> bool:
        return self.min_count <= password.count(self.letter) <= self.max_count

    def _check_position(self, position: int, password: str) -> bool:
        return password[position] == self.letter

    def is_position_valid(self, password: str) -> bool:
        first = self._check_position(self.min_count - 1, password)
        second = self._check_position(self.max_count - 1, password)
        return sum([first, second]) == 1


class PasswordContainer:
    def __init__(self, rule_line: str):
        matches = re.match(r"(\d+)-(\d+) (\S): (\S+)", rule_line)
        min_count, max_count, char, password = matches.groups()
        self.rule = PasswordRule(int(min_count), int(max_count), char)
        self.password = password

    def is_valid_at_old_job(self) -> bool:
        return self.rule.is_count_valid(self.password)

    def is_valid_at_toboggan_corporate(self) -> bool:
        return self.rule.is_position_valid(self.password)


def read_puzzle_input(input_file_path: str) -> List[PasswordContainer]:
    with open(input_file_path, "r") as f:
        return [PasswordContainer(line) for line in f.readlines()]


def count_valid_passwords(containers: List[PasswordContainer],
                          rule: Callable[[PasswordContainer], bool]) -> int:
    return sum(int(rule(container)) for container in containers)


def part_1(inputs: List[PasswordContainer]) -> int:
    return count_valid_passwords(
        inputs,
        lambda container: container.is_valid_at_old_job()
    )


def part_2(inputs: List[PasswordContainer]) -> int:
    return count_valid_passwords(
        inputs,
        lambda container: container.is_valid_at_toboggan_corporate()
    )


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_02.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
