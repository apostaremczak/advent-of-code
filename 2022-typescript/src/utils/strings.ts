export function findAllIndices(str: string, substring: string): number[] {
    const locations = [];
    let idx = -1;
    while ((idx = str.indexOf(substring, idx + 1)) >= 0) {
        locations.push(idx);
    }
    return locations;
}
