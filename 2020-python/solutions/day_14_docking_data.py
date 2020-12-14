import re
import itertools
from typing import List, Dict, Iterable, Tuple
from dataclasses import dataclass

BinaryValue = str


@dataclass
class MemoryOperation:
    position: int
    binary_value: BinaryValue


def int_to_binary(x: int) -> BinaryValue:
    return "{0:b}".format(x).zfill(36)


class Mask:
    def __init__(self, mask: str):
        self.mask = mask
        self.floating_indices = [i for i, bit in enumerate(self.mask)
                                 if bit == "X"]

    def apply(self, value: BinaryValue) -> int:
        output = ""
        for value_bit, mask_bit in zip(value, self.mask):
            if mask_bit != "X":
                output += mask_bit
            else:
                output += value_bit
        return int(output, 2)

    def _modify_single_address(self,
                               address: BinaryValue,
                               bit_values: Iterable[str]) -> BinaryValue:
        modified_address = ""
        last_index = 0
        for position, value in zip(self.floating_indices, bit_values):
            modified_address += (address[last_index:position] + value)
            last_index = position + 1
        modified_address += address[last_index:]
        return modified_address

    def decode_addresses(self, address: int) -> List[int]:
        address_bin = int_to_binary(address)
        masked_address = ""
        for address_bit, mask_bit in zip(address_bin, self.mask):
            if mask_bit == "0":
                masked_address += address_bit
            else:
                masked_address += mask_bit

        # All combinations of floating bit positions
        bit_combinations = itertools.product(
            ["0", "1"], repeat=len(self.floating_indices)
        )

        addresses = []
        for floating_bits in bit_combinations:
            modified_address = self._modify_single_address(masked_address,
                                                           floating_bits)
            addresses.append(modified_address)
        return [int(add, 2) for add in addresses]


def read_puzzle_input(input_file_path: str):
    operations = []
    mask = None
    mask_entries = []

    with open(input_file_path, "r") as f:
        for line in f.readlines():
            if line.startswith("mask"):
                # Save previous group
                if mask_entries and mask:
                    operations.append((mask, mask_entries))

                matches = re.match(r"^mask = (\S{36})$", line)
                mask = Mask(matches.groups()[0])
                mask_entries = []
            else:
                matches = re.match(r"^mem\[(\d+)] = (\d+)$", line)
                position, value = matches.groups()
                binary_value = int_to_binary(int(value))
                mask_entries.append(
                    MemoryOperation(int(position), binary_value)
                )

    operations.append((mask, mask_entries))
    return operations


def part_1(entries) -> int:
    memory = {}
    for mask, operations in entries:
        for operation in operations:
            memory[operation.position] = mask.apply(operation.binary_value)
    return sum(memory.values())


def part_2(entries) -> int:
    memory = {}
    for mask, operations in entries:
        for operation in operations:
            positions = mask.decode_addresses(operation.position)
            for position in positions:
                memory[position] = int(operation.binary_value, 2)
    return sum(memory.values())


if __name__ == '__main__':
    puzzle_inputs = read_puzzle_input("../puzzle_inputs/day_14.txt")
    print(f"Solution to part 1: {part_1(puzzle_inputs)}")
    print(f"Solution to part 2: {part_2(puzzle_inputs)}")
