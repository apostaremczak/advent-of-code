from solutions.day_02_password_philosophy import *


def test_creating_password_containers():
    rule_line = "2-9 c: ccccccccc"
    parsed_container = PasswordContainer(rule_line)
    assert parsed_container.rule == PasswordRule(2, 9, "c")
    assert parsed_container.password == "ccccccccc"


def test_checking_valid_password_old():
    first = PasswordContainer("1-3 a: abcde")
    assert first.is_valid_at_old_job() is True

    second = PasswordContainer("1-3 b: cdefg")
    assert second.is_valid_at_old_job() is False

    third = PasswordContainer("2-9 c: ccccccccc")
    assert third.is_valid_at_old_job() is True


def test_checking_valid_password_corporate():
    first = PasswordContainer("1-3 a: abcde")
    assert first.is_valid_at_toboggan_corporate() is True

    second = PasswordContainer("1-3 b: cdefg")
    assert second.is_valid_at_toboggan_corporate() is False

    third = PasswordContainer("2-9 c: ccccccccc")
    assert third.is_valid_at_toboggan_corporate() is False
