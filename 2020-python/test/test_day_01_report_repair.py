from solutions.day_01_report_repair import find_summands, find_product_of_summands

test_puzzle_input = [
    1721,
    979,
    366,
    299,
    675,
    1456
]


def test_find_summands():
    two_summands = find_summands(test_puzzle_input, 2020, 2)
    assert len(two_summands) == 1
    assert set(two_summands[0]) == {1721, 299}

    three_summands = find_summands(test_puzzle_input, 2020, 3)
    assert len(three_summands) == 1
    assert set(three_summands[0]) == {979, 366, 675}


def test_find_product_of_summands():
    product_of_two = find_product_of_summands(test_puzzle_input, 2020, 2)
    assert product_of_two == 514579

    product_of_three = find_product_of_summands(test_puzzle_input, 2020, 3)
    assert product_of_three == 241861950
