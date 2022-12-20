// Doubly linked array
export default class Coordinate {
    public next: Coordinate;
    public prev: Coordinate;

    constructor(readonly value: number) {
    }

    public setNext(next: Coordinate) {
        this.next = next;
    }

    public setPrevious(prev: Coordinate) {
        this.prev = prev;
    }
}
