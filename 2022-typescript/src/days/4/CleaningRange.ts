// Dataclass-like0

export class CleaningRange {
    constructor(readonly start: number, readonly end: number) {
    }

    public isFullyContainedWithin(other: CleaningRange): boolean {
        return this.start >= other.start && this.end <= other.end;
    }

    public overlaps(other: CleaningRange): boolean {
        return this.contains(other.start) || other.contains(this.start);
    }

    protected contains(value: number): boolean {
        return this.start <= value && value <= this.end;
    }
}

export function createCleaningRange(range: string): CleaningRange {
    const [start, end] = range.split('-');
    return new CleaningRange(parseInt(start), parseInt(end));
}
