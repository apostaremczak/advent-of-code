with open("input.txt", "r") as file:
    puzzle_input = file.readlines()

fields = {}
max_value = 0
for line in puzzle_input:
    a, operation, b, if_, *condition = line.split(" ")
    start_value = fields.get(a, 0)
    left_value = fields.get(condition[0], 0)
    if not left_value:
        fields[condition[0]] = 0
    if_string = " ".join([str(left_value)] + condition[1:])
    if eval(if_string):
        if operation == "inc":
            fields[a] = start_value + int(b)
        elif operation == "dec":
            fields[a] = start_value - int(b)
    else:
        fields[a] = start_value

    current_max = max(fields.values())
    if current_max > max_value:
        max_value = current_max

print(f"Maximum register value after finishing the process: {current_max}")
print(f"Maximum value during the process: {max_value}")