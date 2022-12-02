import fs from "fs"
import path from "path"
import { ms } from "./format"

type Results = [number, number][]

const resultsPath = path.resolve("cache", "results.json")
let _results: Record<number, Results>

function load() {
  if (!_results) {
    try {
      _results = fs.existsSync(resultsPath)
        ? (JSON.parse(fs.readFileSync(resultsPath, { encoding: "utf-8" })) as Record<number, Results>)
        : {}
    } catch {
      _results = {}
    }
  }
  return _results
}

function loadYear(year: number): Results {
  load()
  if (!_results[year]) _results[year] = []
  return _results[year]
}

function save(year: number, results: Results) {
  loadYear(year)
  _results[year] = results
  fs.writeFileSync(resultsPath, JSON.stringify(_results), { encoding: "utf-8" })
}

export function total(year: number, day?: number) {
  const results = loadYear(year)
  return day
    ? results[day - 1][0] + results[day - 1][1]
    : results.map((tt) => (tt ? tt[0] + tt[1] : 0)).reduce((prev, current) => prev + current)
}

export function addBenchmark(year: number, day: number, part: 1 | 2, duration: number, replace = true) {
  const results = loadYear(year)
  if (!replace && results?.[day - 1]?.[part - 1]) return
  if (!results[day - 1]) results[day - 1] = [0, 0]
  results[day - 1][part - 1] = duration
  save(year, results)
}

function getTable(year: number) {
  const results = loadYear(year)
  const ret: string[][] = [
    [`[${year}](./src/${year})`, "Part1", "Part2", "Total", "Days Total", "Stars"],
    ["---", "---", "---", "---", "---", "---"],
  ]
  let cummulative = 0
  for (let d = 1; d <= 25; d++) {
    const times = results[d - 1]
    if (!times) continue
    const total = times?.[0] + times?.[1]
    cummulative += total
    const row = [
      times ? `[Day ${d}](./src/${year}/day${d}.ts)` : `Day ${d}`,
      ms(times?.[0]),
      ms(times?.[1]),
      total ? `${total > 1 ? "❗️" : "⚡️"} ${ms(total)}` : "",
      ms(cummulative),
      "",
    ]
    if (times?.[0]) row[5] += ":star: "
    if (times?.[1]) row[5] += ":star: "
    ret.push(row)
  }
  return `### :snowflake: ${year}\n${ret.map((row) => `|${row.join(" | ")}|`).join("\n")}`
}

export function updateReadme() {
  const all = load()
  const years = Object.keys(all).map((y) => +y)
  years.sort((a, b) => b - a)

  const md = years.map((year) => getTable(year)).join("\n\n")

  const readmePath = path.resolve("README.md")
  const readme = fs
    .readFileSync(readmePath, { encoding: "utf-8" })
    .replace(
      /<!-- RESULTS:BEGIN -->[\s\S]*<!-- RESULTS:END -->/mu,
      `<!-- RESULTS:BEGIN -->\n${md}\n<!-- RESULTS:END -->`
    )
  fs.writeFileSync(readmePath, readme, { encoding: "utf-8" })
}
