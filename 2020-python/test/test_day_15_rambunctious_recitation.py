from solutions.day_15_rambunctious_recitation import *


def test_play_game():
    assert play_game([0, 3, 6], 4) == 0
    assert play_game([0, 3, 6], 5) == 3
    assert play_game([0, 3, 6], 6) == 3
    assert play_game([0, 3, 6], 7) == 1
    assert play_game([0, 3, 6], 8) == 0
    assert play_game([0, 3, 6]) == 436
    assert play_game([3, 1, 2]) == 1836


if __name__ == '__main__':
    test_play_game()
