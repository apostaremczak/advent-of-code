from collections import Counter
from copy import deepcopy
from dataclasses import dataclass
from enum import Enum
from typing import List, Dict, Optional


class BitCriterion(Enum):
    MOST_COMMON = "most_common"
    LEAST_COMMON = "least_common"


@dataclass
class Parameter:
    bit_criterion: BitCriterion
    default_value: Optional[int] = None

    def filter_by_bit_criteria(self,
                               position: int,
                               position_counter: Counter,
                               numbers: List[str]) -> List[str]:
        most_common, least_common = position_counter.most_common(2)
        if most_common[1] == least_common[1]:
            bit_filter = str(self.default_value)
        else:
            if self.bit_criterion == BitCriterion.MOST_COMMON:
                bit_filter = most_common[0]
            elif self.bit_criterion == BitCriterion.LEAST_COMMON:
                bit_filter = least_common[0]
        return [n for n in numbers if n[position] == bit_filter]

    def find_life_support_rating(self, entries: List[str]) -> int:
        rates = deepcopy(entries)
        for position in range(len(entries[0])):
            counts = count_bits(rates)[position]
            if not len(rates) == 1:
                rates = self.filter_by_bit_criteria(position, counts, rates)
        return int(rates[0], 2)


def read_puzzle_input(input_file_path: str) -> List[str]:
    with open(input_file_path, "r") as f:
        return [x.strip() for x in f.readlines()]


def count_bits(entries: List[str]) -> Dict[int, Counter]:
    bit_counts = {position: Counter() for position in range(len(entries[0]))}
    for entry in entries:
        for position, bit in enumerate(entry):
            bit_counts[position][bit] += 1
    return bit_counts


def part_1(entries: List[str]) -> int:
    bit_counts = count_bits(entries)
    gamma_rate = ""
    epsilon_rate = ""
    for position, counts in bit_counts.items():
        most_common, least_common = counts.most_common(2)
        gamma_rate += most_common[0]
        epsilon_rate += least_common[0]

    return int(gamma_rate, 2) * int(epsilon_rate, 2)


def part_2(entries: List[str]) -> int:
    oxygen_generator_rating = Parameter(
        bit_criterion=BitCriterion.MOST_COMMON, default_value=1)
    oxygen_rate = oxygen_generator_rating.find_life_support_rating(entries)

    carbon_dioxide_scrubber_rating = Parameter(
        bit_criterion=BitCriterion.LEAST_COMMON, default_value=0)
    carbon_dioxide_rate = carbon_dioxide_scrubber_rating.find_life_support_rating(entries)

    return oxygen_rate * carbon_dioxide_rate


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_03.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
