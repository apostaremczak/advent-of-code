import { inspect } from "util"

export function format(obj: unknown) {
  return inspect(obj, {
    depth: 6,
    colors: true,
    compact: true,
    showHidden: false,
    getters: false,
    customInspect: false,
    showProxy: false,
  })
}

export function ms(duration: number) {
  if (!duration) return ""

  // console.log(duration)
  if (duration < 1) {
    duration *= 1000
    return `${Math.round(duration)}µs`
  }
  return `${Math.round(duration * 100) / 100}ms`
}
