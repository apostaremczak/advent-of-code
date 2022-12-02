/* eslint-disable no-process-exit */
import chalk from "colorette"
import dotenv from "dotenv"
import fs from "fs"
import path from "path"
import { total, updateReadme } from "./bench"
import { Day, defaultOptions } from "./day"
import { ms } from "./format"

dotenv.config()

function help() {
  console.log(`Usage: aoc [options] [day]

  --year [year] Defaults to ${defaultOptions.year}       
  --bench       Benchmark the solutions for all days
  --help|-h     Display this help message
  `)
  process.exit(1)
}

function parseArgs(args: string[] = process.argv) {
  args.shift() // Remove node path

  const options = defaultOptions
  let day = 0

  for (let c = 0; c < args.length; c++) {
    const arg = args[c]
    switch (arg) {
      case "--help":
      case "-h":
        help()
        continue

      case "--debug":
        options.debug = true
        continue

      case "--year":
      case "-y":
        options.year = +args[c + 1]
        c++
        continue

      case "--bench":
        options.examples = false
        options.benchmark = {
          replace: true,
          minRuns: 5,
          minTime: 500,
          warmup: 3,
        }
        continue

      default:
        day = +arg
        continue
    }
  }
  return { day, options }
}

export async function run() {
  const { day, options } = parseArgs()
  try {
    let time = 0
    if (day >= 1 && day <= 25) {
      console.log(`🌍 https://adventofcode.com/2022/day/${day}`)
      console.log("Loading the day")
      const runner = await Day.load(options.year, day)
      console.log("Loaded the day")
      await runner.run(options)
      time = total(options.year, day)
    } else {
      for (let d = 1; d <= 25; d++) {
        const f = path.resolve("src", `${options.year}`, `day${d}.ts`)
        if (fs.existsSync(f)) {
          const runner = await Day.load(options.year, d)
          await runner.run(options)
        }
      }
      time = total(options.year)
    }
    // updateReadme()
    console.log(`🎅 🎄 done in ${ms(time)}!`)
  } catch (error) {
    if (error instanceof Error) console.log(chalk.red("✖ ") + error.message)
    else console.log(`${chalk.red("✖ error: ")}${error}`)
    if (options.debug) throw error
  }
}
void run()
