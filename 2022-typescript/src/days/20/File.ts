import Coordinate from './Coordinate';

export default class File {
    private values: number[];
    private head: Coordinate;
    private tail: Coordinate;
    private zeroNode: Coordinate;

    constructor(input: string) {
        this.values = input.split('\n').map(n => Number(n));
        const firstNode = new Coordinate(this.values[0]);

        let prevNode = firstNode;
        for (const currentValue of this.values.slice(1)) {
            const currentNode = new Coordinate(currentValue);
            currentNode.setPrevious(prevNode);
            prevNode.setNext(currentNode);

            if (currentValue === 0) {
                this.zeroNode = currentNode;
            }

            prevNode = currentNode;
        }

        const lastNode = prevNode;
        firstNode.setPrevious(lastNode);
        lastNode.setNext(firstNode);

        this.head = firstNode;
        this.tail = lastNode;
    }

    private getNodesInOrder(start = this.head): Coordinate[] {
        const order = [start];
        let current = start;
        let next = current.next;

        while (next !== start) {
            current = next;
            order.push(next);
            next = current.next;
        }

        return order;
    }

    public print(start = this.head): void {
        const values = this.getNodesInOrder(start).map(c => c.value);
        console.log(values.join(', '));
    }

    public getGroveCoordinates(at = [1000, 2000, 3000]): number[] {
        const groveCoords: number[] = [];
        const start = this.zeroNode;

        let i = 1;
        let currentNode = start.next;
        while (groveCoords.length < at.length) {
            if (at.includes(i)) {
                groveCoords.push(currentNode.value);
            }
            currentNode = currentNode.next;
            i += 1;
        }

        return groveCoords;
    }

    private shiftRight(coordinate: Coordinate): void {
        const leftNode = coordinate.prev;
        const rightNode = coordinate.next;
        const rightRightNode = rightNode.next;

        leftNode.setNext(rightNode);
        rightNode.setPrevious(leftNode);

        coordinate.setPrevious(rightNode);
        rightNode.setNext(coordinate);

        coordinate.setNext(rightRightNode);
        rightRightNode.setPrevious(coordinate);
    }

    private shiftLeft(coordinate: Coordinate): void {
        const leftNode = coordinate.prev;
        const leftLeftNode = leftNode.prev;
        const rightNode = coordinate.next;

        leftLeftNode.setNext(coordinate);
        coordinate.setPrevious(leftLeftNode);

        coordinate.setNext(leftNode);
        leftNode.setPrevious(coordinate);

        leftNode.setNext(rightNode);
        rightNode.setPrevious(leftNode);
    }

    public mix(): void {
        // Iterate over all the inputs and move them accordingly
        const originalOrder = this.getNodesInOrder();

        for (const currentNode of originalOrder) {
            if (currentNode.value > 0) {
                Array(currentNode.value).fill(0).forEach(_ => this.shiftRight(currentNode));
            } else if (currentNode.value < 0) {
                Array(Math.abs(currentNode.value)).fill(0).forEach(_ => this.shiftLeft(currentNode));
            }
        }
    }
}
