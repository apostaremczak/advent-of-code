import re
from typing import Dict, List


class Passport:
    VALID_EYE_COLORS = ("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    VALID_NUMS = {str(i) for i in range(10)}
    VALID_HASH_LETTERS = {'a', 'b', 'c', 'd', 'e', 'f'}
    VALID_HASH_CARS = VALID_NUMS.union(VALID_HASH_LETTERS)

    def __init__(self, data_chunk: str):
        self.fields: Dict[str, str] = {
            field_match.group(1): field_match.group(2)
            for field_match in re.finditer(r"(\S+):(\S+)", data_chunk)
        }

    def _get_field(self, field_name: str) -> str:
        return self.fields.get(field_name, "0")

    def _get_int_field(self, field_name: str) -> int:
        return int(self._get_field(field_name))

    def _assert_all_valid_fields(self):
        """
        Throws ugly assertion errors when any field is invalid
        """
        # byr (Birth Year) - four digits; at least 1920 and at most 2002.
        birth_year = self._get_int_field("byr")
        assert 1920 <= birth_year <= 2002

        # iyr (Issue Year) - four digits; at least 2010 and at most 2020.
        issue_year = self._get_int_field("iyr")
        assert 2010 <= issue_year <= 2020

        # eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
        exp_year = self._get_int_field("eyr")
        assert 2020 <= exp_year <= 2030

        # hgt (Height) - a number followed by either cm or in:
        #  - If cm, the number must be at least 150 and at most 193.
        #  - If in, the number must be at least 59 and at most 76.
        height = self._get_field("hgt")
        is_metric = height.endswith("cm")
        is_imperial = height.endswith("in")
        assert is_metric or is_imperial

        height_num = height[:-2]
        if is_metric:
            assert 150 <= int(height_num) <= 193
        if is_imperial:
            assert 59 <= int(height_num) <= 76

        # hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
        hair_col = self._get_field("hcl")
        assert hair_col.startswith("#")
        color_hash = hair_col[1:]
        assert len(color_hash) == 6
        assert set(color_hash).issubset(Passport.VALID_HASH_CARS)

        # ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
        eye_col = self._get_field("ecl")
        assert eye_col in Passport.VALID_EYE_COLORS

        # pid (Passport ID) - a nine-digit number, including leading zeroes.
        pass_id = self._get_field("pid")
        assert len(pass_id) == 9
        assert set(pass_id).issubset(Passport.VALID_NUMS)

    def _has_cid(self) -> bool:
        return self.fields.get("cid") is not None

    def is_valid_naive(self) -> bool:
        num_fields = len(self.fields)
        all_fields = num_fields == 8
        only_cid_missing = num_fields == 7 and not self._has_cid()
        return all_fields or only_cid_missing

    def is_valid(self) -> bool:
        try:
            self._assert_all_valid_fields()
        except AssertionError:
            return False
        return True


def read_puzzle_input(input_file_path: str) -> List[Passport]:
    with open(input_file_path, "r") as f:
        return [
            Passport(chunk)
            for chunk in f.read().split("\n\n")
        ]


def part_1(passports: List[Passport]) -> int:
    return sum(
        passport.is_valid_naive()
        for passport in passports
    )


def part_2(passports: List[Passport]) -> int:
    return sum(
        passport.is_valid()
        for passport in passports
    )


if __name__ == '__main__':
    passport_entries = read_puzzle_input("../puzzle_inputs/day_04.txt")
    print(f"Solution to part 1: {part_1(passport_entries)}")
    print(f"Solution to part 2: {part_2(passport_entries)}")
