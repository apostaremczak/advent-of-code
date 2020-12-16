import re
from math import prod
from typing import List, Set, Tuple


class Field:
    def __init__(self, config: str):
        matches = re.fullmatch(r"(.*): (\d+)-(\d+) or (\d+)-(\d+)", config)
        name, low_1, high_1, low_2, high_2 = matches.groups()
        self.name = name
        self.in_range_1 = lambda x: int(low_1) <= x <= int(high_1)
        self.in_range_2 = lambda x: int(low_2) <= x <= int(high_2)

    def satisfied_by(self, x: int) -> bool:
        return self.in_range_1(x) or self.in_range_2(x)

    def __eq__(self, other):
        return self.name == other.name

    def __hash__(self):
        return hash(self.name)


class Ticket:
    def __init__(self, config: str, fields: List[Field]):
        self.field_values = [int(x) for x in config.split(",") if x]
        self.suitable_fields = self._get_suitable_fields(fields)
        self.invalid_values = self._get_invalid_values()

    def _get_suitable_fields(self, fields: List[Field]) -> List[Set[Field]]:
        return [
            {
                field
                for field in fields
                if field.satisfied_by(field_value)
            }
            for field_value in self.field_values
        ]

    def _get_invalid_values(self):
        return [
            val
            for val, fields in zip(self.field_values, self.suitable_fields)
            if not fields
        ]


def read_puzzle_input(input_file_path: str):
    with open(input_file_path, "r") as f:
        fields, my_ticket, tickets_config = f.read().split("\n\n")

    fields = [Field(field_config) for field_config in fields.split("\n")]
    my_ticket = Ticket(my_ticket.split("\n")[1], fields)
    tickets_config = [
        Ticket(t, fields) for t in tickets_config.split("\n")[1:] if t
    ]

    return fields, my_ticket, tickets_config


def part_1(tickets: List[Ticket]) -> int:
    return sum(sum(ticket.invalid_values) for ticket in tickets)


def part_2(fields: List[Field], my_ticket: Ticket,
           tickets: List[Ticket]) -> int:
    valid_tickets = [t for t in tickets if not t.invalid_values] + [my_ticket]

    # Which fields match position x in all tickets?
    matching_fields: List[Tuple[int, Set[Field]]] = [
        (p, set.intersection(*[t.suitable_fields[p] for t in valid_tickets]))
        for p in range(len(fields))
    ]
    matching_fields = sorted(matching_fields, key=lambda s: len(s[1]))

    departure_values = []
    while matching_fields:
        index, i_fields = matching_fields[0]
        field = i_fields.pop()
        if field.name.startswith("departure"):
            departure_values.append(my_ticket.field_values[index])
        matching_fields = [
            (p, f.difference({field}))
            for p, f in matching_fields[1:]
        ]

    return prod(departure_values)


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_16.txt")
    print(f"Solution to part 1: {part_1(puzzle_input[-1])}")
    print(f"Solution to part 2: {part_2(*puzzle_input)}")
