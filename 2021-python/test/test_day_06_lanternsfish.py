from solutions.day_06_lanternfish import *


def test_simulating_singe_day():
    school = LanternfishSchool([3, 4, 3, 1, 2])
    school.simulate_single_day()
    assert school == LanternfishSchool([2, 3, 2, 0, 1])

    school.simulate_single_day()
    assert school == LanternfishSchool([1, 2, 1, 6, 0, 8])

    school.simulate_single_day()
    assert school == LanternfishSchool([0, 1, 0, 5, 6, 7, 8])


def test_simulating_multiple_days():
    school = LanternfishSchool([3, 4, 3, 1, 2])
    school.simulate_multiple_days(18)

    assert school == LanternfishSchool([
        6, 0, 6, 4, 5, 6, 0, 1, 1, 2, 6, 0, 1, 1, 1, 2, 2, 3, 3, 4, 6,
        7, 8, 8, 8, 8
    ])


def test_counting_fish():
    school = LanternfishSchool([3, 4, 3, 1, 2])
    assert school.get_current_fish_count() == 5

    school.simulate_multiple_days(80)
    assert school.get_current_fish_count() == 5934

    school.simulate_multiple_days(256 - 80)
    assert school.get_current_fish_count() == 26984457539
