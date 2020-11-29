with open("input.txt", "r") as file:
    puzzle_input = [row.replace("\n", "") for row in file]

# Part 1
is_original = lambda row: len(row.split(" ")) == len(set(row.split(" ")))

print(f"Number of passphrases without duplicates: "
      f"{sum(is_original(phrase) for phrase in puzzle_input)}")

# Part 2
is_valid = lambda row: is_original and not any((len(word1) == len(word2) and all(word1.count(letter) == word2.count(letter) for letter in word1)) for i, word1 in enumerate(row.split(" ")) for word2 in row.split(" ")[:i] + row.split(" ")[i+1:])

print(f"Number of passphrases without anagrams: "
      f"{sum(is_valid(phrase) for phrase in puzzle_input)}")
