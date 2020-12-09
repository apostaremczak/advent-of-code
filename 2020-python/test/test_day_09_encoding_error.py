from solutions.day_09_encoding_error import *

test_inputs = [35, 20, 15, 25, 47, 40, 62, 55, 65, 95, 102, 117, 150, 182, 127,
               219, 299, 277, 309, 576]


def test_find_first_misfit():
    assert find_first_misfit(test_inputs, window_size=5) == 127


def test_find_sum_range():
    min_, max_ = find_sum_range(test_inputs, 127)
    assert min_ == 15
    assert max_ == 47
