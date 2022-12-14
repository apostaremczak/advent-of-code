import Puzzle from '../../types/AbstractPuzzle';
import { sum } from '../../utils/arrays';
import { Packet, areInCorrectOrder } from './Packet';

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const orders = input.split('\n\n').map((pair, index) => {
            const [left, right] = pair.split('\n');
            const leftPacket: Packet = JSON.parse(left);
            const rightPacket: Packet = JSON.parse(right);
            if (areInCorrectOrder(leftPacket, rightPacket)) {
                return index + 1;
            } else {
                return 0;
            }
        });

        return sum(orders).toString();
    }

    public getFirstExpectedResult(): string {
        return '13';
    }

    public solveSecond(input: string): string {
        const firstDivider: Packet = [[2]];
        const secondDivider: Packet = [[6]];
        const packets: Packet[] = input.split('\n\n').flatMap(pair => {
            const [left, right] = pair.split('\n');
            const leftPacket: Packet = JSON.parse(left);
            const rightPacket: Packet = JSON.parse(right);

            return [leftPacket, rightPacket];
        })
            .concat([firstDivider, secondDivider]);

        const orderedPackets = packets
            .sort((a, b) => {
                if (areInCorrectOrder(a, b)) {
                    return -1;
                }
                return 1;
            });

        const decoderKey = (orderedPackets.indexOf(firstDivider) + 1) * (orderedPackets.indexOf(secondDivider) + 1);
        return decoderKey.toString();
    }

    public getSecondExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 2;
        return '140';
    }
}
