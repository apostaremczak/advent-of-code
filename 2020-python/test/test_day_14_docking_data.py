from solutions.day_14_docking_data import *


def test_mask_apply():
    value = "000000000000000000000000000000001011"
    mask = Mask("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X")
    assert mask.apply(value) == int("000000000000000000000000000001001001", 2)


def test_part_1():
    test_input = read_puzzle_input("puzzle_inputs/day_14.txt")
    assert part_1(test_input) == 165


def test_mask_modify_single_address():
    mask = Mask("00X11X")
    address = "00X11X"
    assert mask._modify_single_address(address, ["0", "0"]) == "000110"
    assert mask._modify_single_address(address, ["1", "0"]) == "001110"

    mask_2 = Mask("X0")
    address_2 = "X1"
    assert mask_2._modify_single_address(address_2, ["1"]) == "11"


def test_mask_decode_addresses():
    address_1 = 42
    mask_1 = Mask("000000000000000000000000000000X1001X")
    expected_addresses_1 = {26, 27, 58, 59}
    assert set(mask_1.decode_addresses(address_1)) == expected_addresses_1

    address_2 = 26
    mask_2 = Mask("00000000000000000000000000000000X0XX")
    expected_addresses_2 = {16, 17, 18, 19, 24, 25, 26, 27}
    assert set(mask_2.decode_addresses(address_2)) == expected_addresses_2


def test_part_2():
    test_input = read_puzzle_input("puzzle_inputs/day_14_2.txt")
    assert part_2(test_input) == 208
