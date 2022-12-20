import Coordinate from './Coordinate';

export default class File {
    private readonly values: number[];
    private head: Coordinate;
    private readonly zeroNode: Coordinate;
    private readonly originalOrder: Coordinate[];
    private readonly size: number;

    constructor(input: string, readonly decryptionKey = 1) {
        this.values = input.split('\n').map(n => (Number(n) * decryptionKey));
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
        this.originalOrder = this.getNodesInOrder();
        this.size = this.originalOrder.length - 1;
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
        for (const currentNode of this.originalOrder) {
            if (currentNode.value > 0) {
                for (let i = 1; i <= currentNode.value % this.size; i++) {
                    this.shiftRight(currentNode);
                }
            } else if (currentNode.value < 0) {
                for (let i = 1; i <= Math.abs(currentNode.value) % this.size; i++) {
                    this.shiftLeft(currentNode);
                }
            }
        }
    }
}
