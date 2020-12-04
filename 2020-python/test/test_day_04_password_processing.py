from solutions.day_04_passport_processing import *

pass_1 = Passport(
    """
    ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
    byr:1937 iyr:2017 cid:147 hgt:183cm
    """
)

pass_2 = Passport(
    """
    iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
    hcl:#cfa07d byr:1929
    """
)

pass_3 = Passport(
    """
    hcl:#ae17e1 iyr:2013
    eyr:2024
    ecl:brn pid:760753108 byr:1931
    hgt:179cm
    """
)

pass_4 = Passport(
    """
    hcl:#cfa07d eyr:2025 pid:166559648
    iyr:2011 ecl:brn hgt:59in
    """
)


def test_naive_validity():
    assert pass_1.is_valid_naive() is True
    assert pass_2.is_valid_naive() is False
    assert pass_3.is_valid_naive() is True
    assert pass_4.is_valid_naive() is False


def test_part_1():
    passports = [pass_1, pass_2, pass_3, pass_4]
    assert part_1(passports) == 2


def test_full_validity():
    valid_passwords = [
        Passport(
            """
            pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
            hcl:#623a2f
            """
        ),
        Passport(
            """
            eyr:2029 ecl:blu cid:129 byr:1989
            iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
            """
        ),
        Passport(
            """
            hcl:#888785
            hgt:164cm byr:2001 iyr:2015 cid:88
            pid:545766238 ecl:hzl
            eyr:2022
            """
        ),
        Passport(
            """
            iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 
            pid:093154719
            """
        )
    ]

    assert all(p.is_valid() for p in valid_passwords)

    invalid_passports = [
        Passport(
            """
            eyr:1972 cid:100
            hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
            """
        ),
        Passport(
            """
            eyr:1972 cid:100
            hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
            """
        ),
        Passport(
            """       
            iyr:2019
            hcl:#602927 eyr:1967 hgt:170cm
            ecl:grn pid:012533040 byr:1946
            """
        ),
        Passport(
            """
            hcl:dab227 iyr:2012
            ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
            """
        ),
        Passport(
            """
            hgt:59cm ecl:zzz
            eyr:2038 hcl:74454a iyr:2023
            pid:3556412378 byr:2007
            """
        )
    ]

    assert all(not p.is_valid() for p in invalid_passports)
