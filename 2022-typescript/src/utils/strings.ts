export function findAllIndices(str: string, substring: string): number[] {
    const locations = [];
    let idx = -1;
    while ((idx = str.indexOf(substring, idx + 1)) >= 0) {
        locations.push(idx);
    }
    return locations;
}


export function findAllGroups(str: string, regex: RegExp): string[] {
    return Array.from(str.matchAll(regex))[0];
}
