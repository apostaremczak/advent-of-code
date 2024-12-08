# Helper script for parsing string grids into CSV files for easy coordinate debugging with Google Sheets
# (I hate counting characters in a line to find out coordinates in the examples)
import csv

with open("grid.txt", "r") as f:
    lines = f.readlines()

two_d_array = [[c for c in line.strip()] for line in lines if line]

with open('grid.csv', 'w', newline='') as f:
    writer = csv.writer(f)
    writer.writerows(two_d_array)
