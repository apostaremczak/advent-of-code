export * from "./input"
export * from "./day"
export * from "./format"
export * from "./bench"
export * from "./grid"

export type FieldState = { offset: number }
export function field(state: FieldState, str: string, delims: string[]): string {
  let end = str.length
  for (const delim of delims) {
    const i = str.indexOf(delim, state.offset)
    if (i != -1 && i < end) end = i
  }
  const ret = str.slice(state.offset, end)
  state.offset = end + 1
  return ret
}
