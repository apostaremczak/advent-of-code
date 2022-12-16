import Puzzle from '../../types/AbstractPuzzle';
import { Coord } from '../../utils/mazes';
import { deduplicate, sum } from '../../utils/arrays';
import SensorBall from './SensorBall';

const instructionRegex = /Sensor at x=(\d+|-\d+), y=(\d+|-\d+): closest beacon is at x=(\d+|-\d+), y=(\d+|-\d+)/g;

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const inputLines = input.split('\n');
        const rowY = Number(inputLines.pop().split(';')[0]);
        const sensorBalls: SensorBall[] = this.parseSensorBalls(inputLines);

        const beaconsAtRowY: number[] = deduplicate(
            sensorBalls
                .filter(b => b.closestBeacon.y === rowY)
                .map(ball => ball.closestBeacon.x)
        );

        const unbeacableRanges = sensorBalls
            .map(ball => ball.getCoveredRangeAt(rowY))
            .filter(r => r !== null);

        // To be fully pedantic, we should have considered an algebraic sum of all the intervals found above
        // (For this, see the unused method getIntervalUnionLength)
        // However, in this example, the resulting interval is fully continuous and doesn't have any holes
        // Thus finding the total length requires us to simply find the outermost borders
        const xStart = Math.min(...unbeacableRanges.map(r => r.start));
        const xEnd = Math.max(...unbeacableRanges.map(r => r.stop));
        const beaconsInRange = beaconsAtRowY.filter(b => b >= xStart && b <= xEnd);

        // Add plus one because for each interval of length n there are n + 1 possible beacon positions
        return (Math.abs(xEnd - xStart) + 1 - beaconsInRange.length).toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '26';
    }

    public solveSecond(input: string): string {
        const inputLines = input.split('\n');
        const minCoord = 0;
        const maxCoord = Number(inputLines.pop().split(';')[1]);
        const sensorBalls: SensorBall[] = this.parseSensorBalls(inputLines);

        for (const [index, ball] of sensorBalls.entries()) {
            const border = ball.getOutsideBorder();
            const otherBalls = sensorBalls.slice(0, index).concat(sensorBalls.slice(index + 1, sensorBalls.length));
            for (const p of border) {
                if (p.x >= minCoord && p.x <= maxCoord && p.y >= minCoord && p.y <= maxCoord) {
                    const withinOtherBalls = otherBalls.find(b => b.isWithinRadius(p));
                    if (withinOtherBalls === undefined) {
                        return (p.x * 4000000 + p.y).toString();
                    }
                }
            }
        }
    }

    public getSecondExpectedResult(): string {
        return '56000011';
    }

    private parseSensorBalls(inputLines: string[]): SensorBall[] {
        return inputLines.map(line => {
            const matches = Array.from(line.matchAll(instructionRegex))[0];
            const sensor: Coord = {
                x: Number(matches[1]),
                y: Number(matches[2])
            };
            const beacon: Coord = {
                x: Number(matches[3]),
                y: Number(matches[4])
            };

            return new SensorBall(sensor, beacon);
        });
    }

    private getIntervalUnionLength(intervals: { start: number, end: number }[]): number {
        const sortedIntervals = intervals
            .sort((a, b) => {
            if (a.start < b.start) {
                return -1;
            }
            if (a.start === b.start) {
                return 0;
            }
            return 1;
        });

        const intervalUnion = [];
        let currentStart = sortedIntervals[0].start;
        let currentStop = sortedIntervals[0].end;

        for (const { start: start, end: stop } of sortedIntervals.slice(1, sortedIntervals.length)) {
            // Check if the current interval overlaps with the current
            if (start <= currentStop) {
                if (stop > currentStop) {
                    currentStop = stop;
                }
            } else {
                // No overlapping - start a new interval and save the previous one
                intervalUnion.push({
                    start: currentStart,
                    end: currentStop
                });
                currentStart = start;
                currentStop = stop;
            }

        }
        intervalUnion.push({
            start: currentStart,
            end: currentStop
        });

        return sum(intervalUnion.map(i => Math.abs(i.end - i.start) + 1));
    }

}
