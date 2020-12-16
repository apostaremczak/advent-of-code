from solutions.day_16_ticket_translation import *


def test_ticket_get_invalid_values():
    fields, my_ticket, tickets = read_puzzle_input("puzzle_inputs/day_16.txt")
    assert tickets[0].invalid_values == []
    assert tickets[1].invalid_values == [4]
    assert tickets[2].invalid_values == [55]
    assert tickets[3].invalid_values == [12]


def test_part_1():
    _, _, tickets = read_puzzle_input("puzzle_inputs/day_16.txt")
    assert part_1(tickets) == 71
