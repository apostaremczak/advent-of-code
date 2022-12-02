import { CustomInspectFunction, inspect, InspectOptionsStylized } from "util"

export type TypedArray<T> = (
  | Int8Array
  | Uint8Array
  | Int16Array
  | Uint16Array
  | Int32Array
  | Uint32Array
  | Uint8ClampedArray
  | Float32Array
  | Float64Array
) & { [index: number]: T }

export type ReadonlyData<T> = ArrayLike<T> & {
  [Symbol.iterator](): IterableIterator<T>
  slice(start?: number, end?: number): ReadonlyData<T>
}

export type Data<T> = (Array<T> | TypedArray<T>) & {
  [Symbol.iterator](): IterableIterator<T>
  slice(start?: number, end?: number): Data<T>
}

const defaultOptions = { inspect: { joinRows: false } }
export type GridOptions = typeof defaultOptions

export class ReadonlyGrid<T> {
  options: GridOptions
  height: number
  _hi: [number, number] = [0, 0]
  _lo: [number, number] = [0, 0]
  private _width: number

  constructor(public data: ReadonlyData<T>, public width: number, options?: Partial<GridOptions>) {
    this._width = width
    this.options = { ...defaultOptions, ...options }
    this.height = Math.ceil(data.length / width)
  }

  hi(x: number, y: number) {
    this._hi[0] += x
    this._hi[1] += y
    this.width -= x
    this.height -= y
    return this
  }

  lo(x: number, y: number) {
    this._lo[0] += x
    this._lo[1] += y
    this.width -= x
    this.height -= y
    return this
  }

  index(x: number, y: number): number {
    return (y + this._lo[1]) * this._width + x + this._lo[0]
  }

  cell(index: number): [number, number] {
    const w = this.width + this._lo[0] + this._hi[0]
    const y = Math.floor(index / w)
    const x = index % w
    return [x - this._lo[0], y - this._lo[1]]
  }

  // private valid(index: number) {
  //   const [x, y] = this.cell(index)
  //   // console.log([x, y])
  //   return x >= 0 && y >= 0 && x < this.width && y < this.height
  // }

  valid(x: number, y: number) {
    return x >= 0 && y >= 0 && x < this.width && y < this.height
  }

  forEach(cb: (value: T, x: number, y: number, index: number) => void): void {
    for (let y = 0; y < this.height; y++) {
      for (let x = 0; x < this.width; x++) {
        const index = this.index(x, y)
        cb(this.data[index], x, y, index)
      }
    }
  }

  map<U>(cb: (value: T, x: number, y: number, index: number) => U): Grid<U> {
    const data: U[] = []
    for (let y = 0; y < this.height; y++) {
      for (let x = 0; x < this.width; x++) {
        const index = this.index(x, y)
        data.push(cb(this.data[index], x, y, index))
      }
    }
    return new Grid(data, this.width, this.options)
  }

  *values() {
    for (let y = 0; y < this.height; y++) {
      for (let x = 0; x < this.width; x++) {
        yield this.get(x, y)
      }
    }
  }

  *adjacent(x: number, y: number): Generator<[T, number, number], void, unknown> {
    for (const yy of [y - 1, y, y + 1]) {
      for (const xx of [x - 1, x, x + 1]) {
        if (xx == x && yy == y) continue
        const index = this.index(xx, yy)
        if (!this.valid(xx, yy)) continue
        yield [this.data[index], xx, yy]
      }
    }
  }

  get(x: number, y: number): T {
    return this.data[this.index(x, y)]
  }

  get length(): number {
    return this.data.length
  }

  flatten() {
    return [...this.values()]
  }

  clone(readonly?: true): ReadonlyGrid<T>
  clone(readonly: false): Grid<T>
  clone(readonly = true) {
    const ro = !(this instanceof Grid)
    if (readonly) {
      const ret = new ReadonlyGrid(
        ro ? this.data : this.data.slice(),
        this.width + this._lo[0] + this._hi[0],
        this.options
      )
      ret.lo(...this._lo)
      ret.hi(...this._hi)
      return ret
    } else return new Grid(this.flatten(), this.width, this.options)
  }

  *rows() {
    for (let y = 0; y < this.height; y++) yield this.data.slice(this.index(0, y), this.index(0, y) + this.width)
  }

  [inspect.custom]: CustomInspectFunction = (depth: number, options: InspectOptionsStylized): string => {
    const name = this instanceof Grid ? "Grid" : "*Grid"
    let ret = options.stylize(`${name}(`, "special")
    ret += [...this.rows()]
      .map((row) => {
        let line: string | ReadonlyData<T> = row
        if (this.options?.inspect.joinRows) line = Array.isArray(row) ? row.join("") : row
        return inspect(line, {
          ...options,
          depth: depth - 1,
          breakLength: 200,
          compact: true,
        })
      })
      .join(`\n${" ".repeat(name.length + 1)}`)
    ret += options.stylize(")", "special")
    return ret
  }
}

export class Grid<T> extends ReadonlyGrid<T> {
  constructor(public data: Data<T>, public width: number, options?: GridOptions) {
    super(data, width, options)
  }

  set(x: number, y: number, val: T) {
    this.data[this.index(x, y)] = val
  }

  static fromString(data: string, delimiter = "\n") {
    return new ReadonlyGrid(data, data.indexOf(delimiter) + 1, {
      inspect: { joinRows: true },
    }).hi(1, 0)
  }
}
