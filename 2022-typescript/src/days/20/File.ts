import { toSlidingWindows } from '../../utils/arrays';
import Coordinate from './Coordinate';

export default class File {
    private values: number[];
    private coords: Map<number, Coordinate>;

    constructor(input: string) {
        this.values = input.split('\n').map(n => Number(n));
        this.coords = this.inputToDoublyLinked(this.values);
    }

    // Create a doubly linked array
    private inputToDoublyLinked(values: number[]): Map<number, Coordinate> {
        const m = new Map<number, Coordinate>();

        // Create the node for the first entry
        const firstNode = new Coordinate(values[0]);
        m.set(values[0], firstNode);

        toSlidingWindows(values, 2).forEach(([previousValue, currentValue], _) => {
            const currentNode = new Coordinate(currentValue);
            const prevNode = m.get(previousValue);
            currentNode.setPrevious(prevNode);
            prevNode.setNext(currentNode);

            m.set(currentValue, currentNode);
        });

        // Link the first and the last nodes to create a circular array
        const lastNode = m.get(values.slice(-1)[0]);
        firstNode.setPrevious(lastNode);
        lastNode.setNext(firstNode);

        return m;
    }

    public getGroveCoordinates(
        start = 0,
        at = [1000, 2000, 3000]
    ): number[] {
        const groveCoords: number[] = [];

        let i = 1;
        let currentNode = this.coords.get(start).next;
        while (groveCoords.length < at.length) {
            if (at.includes(i)) {
                groveCoords.push(currentNode.value);
            }
            currentNode = currentNode.next;
            i += 1;
        }

        return groveCoords;
    }

    private shiftRight(startPosition: number): void {
        const middleNode = this.coords.get(startPosition);
        const leftNode = middleNode.prev;
        const rightNode = middleNode.next;
        const rightRightNode = rightNode.next;

        leftNode.setNext(rightNode);
        rightNode.setPrevious(leftNode);

        middleNode.setPrevious(rightNode);
        rightNode.setNext(middleNode);

        middleNode.setNext(rightRightNode);
        rightRightNode.setPrevious(middleNode);
    }

    private shiftLeft(startPosition: number): void {
        const middleNode = this.coords.get(startPosition);
        const leftNode = middleNode.prev;
        const leftLeftNode = leftNode.prev;
        const rightNode = middleNode.next;

        leftLeftNode.setNext(middleNode);
        middleNode.setPrevious(leftLeftNode);

        middleNode.setNext(leftNode);
        leftNode.setPrevious(middleNode);

        leftNode.setNext(rightNode);
        rightNode.setPrevious(leftNode);
    }

    public mix(): void {
        // Iterate over all the inputs and move them accordingly
        for (const currentValue of this.values) {
            if (currentValue > 0) {
                // Move to the right
                Array(currentValue).fill(0).forEach(_ => this.shiftRight(currentValue));
            } else if (currentValue < 0) {
                // Move to the left
                Array(Math.abs(currentValue)).fill(0).forEach(_ => this.shiftLeft(currentValue));
            }
        }
    }

    public print(start: number = this.values[0]): void {
        const values = [start];
        let current = start;
        let next = this.coords.get(current).next.value;

        while (next !== start) {
            current = next;
            values.push(next);
            next = this.coords.get(current).next.value;
        }

        console.log(values.join(', '));
    }
}
