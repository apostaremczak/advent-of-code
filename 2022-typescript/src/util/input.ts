import fs from "fs"
import fetch from "node-fetch"
import path from "path"
import { Grid } from "./grid"

export class Input {
  lines: string[]

  public constructor(public data: string) {
    this.lines = this.data.trim().split("\n")
  }

  strings(separator: string | RegExp = "\n") {
    if (separator == "\n") return this.lines
    return this.data.trim().split(separator)
  }

  numbers(separator: string | RegExp = "\n") {
    return this.strings(separator).map((n) => +n)
  }

  grid() {
    return Grid.fromString(this.data)
  }

  public static async get(year: number, day: number) {
    const cachePath = path.resolve("cache", `${year}`)
    if (!fs.existsSync(cachePath)) fs.mkdirSync(cachePath, { recursive: true })

    const cacheFile = path.resolve(cachePath, `day${day}.txt`)
    if (!fs.existsSync(cacheFile)) {
      fs.writeFileSync(cacheFile, await this._fetch(year, day), {
        encoding: "utf-8",
      })
    }
    return new Input(fs.readFileSync(cacheFile, { encoding: "utf-8" }))
  }

  private static async _fetch(year: number, day: number) {
    const session = process.env.AOC_SESSION
    if (!session) throw new Error("Please specify AOC_SESSION in your .env file")
    return await fetch(`https://adventofcode.com/${year}/day/${day}/input`, {
      headers: { cookie: `session=${session}` },
    })
      .then((res) => {
        if (!res.ok) throw new Error(`Could not fetch input for day ${day} (${res.status} ${res.statusText})`)
        return res
      })
      .then((res) => res.text())
  }
}
