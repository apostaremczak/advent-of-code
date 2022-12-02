import { Grid, ReadonlyData, ReadonlyGrid } from "../src/util"

const cases: [ReadonlyData<unknown>, number, number][] = [
  ["123456789", 3, 3],
  [new Uint32Array([1, 2, 3, 4, 5]), 3, 2],
  [[0, 1, 2, 3, 4, 5], 2, 3],
  ["abcde", 6, 1],
  [Buffer.from("abcdef"), 3, 2],
]

test.each(cases)("Grid#%# %p (%ix%i)", (data, width, height) => {
  const grid = new ReadonlyGrid(data, width)
  expect(grid.data).toHaveLength(data.length)
  expect(grid.width).toBe(width)
  expect(grid.height).toBe(height)

  for (let x = 0; x < grid.width; x++) {
    for (let y = 0; y < grid.height; y++) {
      expect(grid.get(x, y)).toBe(data[grid.index(x, y)])
    }
  }

  const clone = grid.clone().data
  // eslint-disable-next-line unicorn/no-for-loop
  for (let i = 0; i < data.length; i++) {
    expect(clone[i]).toBe(data[i])
  }

  const arr = grid.flatten()
  // eslint-disable-next-line unicorn/no-for-loop
  for (let i = 0; i < data.length; i++) {
    expect(arr[i]).toBe(data[i])
  }
})

test("index vs cell", () => {
  const grid = new ReadonlyGrid("123456789", 3)
  for (let x = 0; x < 3; x++) {
    for (let y = 0; y < 3; y++) {
      expect(grid.index(x, y)).toBe(y * 3 + x)
    }
  }
})

test("hi", () => {
  const grid = new Grid("123456789".split(""), 3)
  expect([...grid.hi(1, 0).values()]).toEqual(["1", "2", "4", "5", "7", "8"])
})

// console.log(
//   Grid.fromString(
//     `L.LL.LL.LL
// LLLLLLL.LL
// L.L.L..L..
// LLLL.LL.LL
// L.LL.LL.LL
// L.LLLLL.LL
// ..L.L.....
// LLLLLLLLLL
// L.LLLLLL.L
// L.LLLLL.LL`
//   )
// )
