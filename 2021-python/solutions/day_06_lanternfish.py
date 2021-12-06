from typing import List


class LanternfishSchool:
    NEW_FISH_TIMER: int = 8
    FISH_TIMER_RESET: int = 6

    def __init__(self, initial_timers: List[int]):
        self.timers = {
            days_left: sum(timer == days_left for timer in initial_timers)
            for days_left in range(9)
        }

    def __str__(self):
        return str(self.timers)

    def __eq__(self, other):
        return self.timers == other.timers

    def simulate_single_day(self) -> None:
        new_timers = {
            days_left - 1: self.timers[days_left]
            for days_left in range(1, 9)
        }
        new_fish = self.timers[0]
        new_timers[LanternfishSchool.FISH_TIMER_RESET] += new_fish
        new_timers[LanternfishSchool.NEW_FISH_TIMER] = new_fish
        self.timers = new_timers

    def simulate_multiple_days(self, num_days: int) -> None:
        for _ in range(num_days):
            self.simulate_single_day()

    def get_current_fish_count(self) -> int:
        return sum(self.timers.values())


def read_puzzle_input(input_file_path: str) -> List[int]:
    with open(input_file_path, "r") as f:
        return [
            int(x)
            for line in f.readlines()
            for x in line.strip().split(",")
        ]


def part_1(entries: List[int]) -> int:
    school = LanternfishSchool(entries)
    school.simulate_multiple_days(80)
    return school.get_current_fish_count()


def part_2(entries: List[int]) -> int:
    school = LanternfishSchool(entries)
    school.simulate_multiple_days(256)
    return school.get_current_fish_count()


if __name__ == '__main__':
    puzzle_input = read_puzzle_input("../puzzle_inputs/day_06.txt")
    print(f"Solution to part 1: {part_1(puzzle_input)}")
    print(f"Solution to part 2: {part_2(puzzle_input)}")
