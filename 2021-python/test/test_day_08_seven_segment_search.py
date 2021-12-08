from solutions.day_08_seven_segment_search import *


def test_part_1():
    entries = read_puzzle_input("puzzle_inputs/day_08.txt")
    assert part_1(entries) == 26


def test_find_symbol_map():
    expected_map = {'d': 'a', 'e': 'b', 'a': 'c', 'f': 'd', 'g': 'e', 'b': 'f', 'c': 'g'}
    returned_map = find_symbol_map(
        "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab cdfeb fcadb cdfeb cdbaf".split()
    )
    assert expected_map == returned_map


def test_decode_number():
    symbol_map = {'d': 'a', 'e': 'b', 'a': 'c', 'f': 'd', 'g': 'e', 'b': 'f', 'c': 'g'}
    assert decode_number(["acedgfb"], symbol_map) == 8
    assert decode_number(["cdfbe"], symbol_map) == 5
    assert decode_number(["gcdfa"], symbol_map) == 2
    assert decode_number("cdfeb fcadb cdfeb cdbaf".split(), symbol_map) == 5353


def test_part_2():
    entries = read_puzzle_input("puzzle_inputs/day_08.txt")
    assert part_2(entries) == 61229
