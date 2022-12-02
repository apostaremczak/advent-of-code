# Advent of Code 2020 :santa: :christmas_tree: :snowman: :sparkles: 

[Advent of Code](https://adventofcode.com/) using TypeScript.
This repository was created using the [AoC-TypeScript template by folke](https://github.com/folke/adventofcode).

There's no need to *build* anything. Source files are transpiled and cached on-the-fly using [esbuild-runner](https://github.com/folke/esbuild-runner/) with pretty much **zero overhead**. Running the code for a certain day, will:

* download the input if needed
* test any examples for each part
* test the answer (if available) for each part
* measure the performance and update the benchmark section of this readme

## :rocket: Usage

```shell
$ bin/aoc --help
Usage: aoc [options] [day]

  --year [year] Defaults to 2022
  --bench       Benchmark the solutions for all days
  --help|-h     Display this help message
```

To run all days:

```shell
$ bin/aoc
```

To run a specific day:

```shell
$ bin/aoc 3
```

To run the benchmarks:

```shell
$ bin/aoc --bench
```

> if a day has not been implemented yet, executing that day will create a new `src/day??.ts` file based on the [template](src/day.template.ts)
