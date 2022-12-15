import Puzzle from '../../types/AbstractPuzzle';
import { Coord, coordToStr } from '../../utils/mazes';
import { deduplicate } from '../../utils/arrays';
import SensorBall from './SensorBall';

const instructionRegex = /Sensor at x=(\d+|-\d+), y=(\d+|-\d+): closest beacon is at x=(\d+|-\d+), y=(\d+|-\d+)/g;

export default class ConcretePuzzle extends Puzzle {
    public solveFirst(input: string): string {
        const sensorBalls: SensorBall[] = this.parseSensorBalls(input);
        const rowY = 10; // TEST
        // const rowY = 2000000; // Actual solution

        const beacons: string[] = sensorBalls.map(ball => coordToStr(ball.closestBeacon));
        const unbeacableCoords: string[] = sensorBalls
            .flatMap(ball => ball.getUnbeacablePointsAt(rowY, ball.sensor.x - ball.radius, ball.sensor.x + ball.radius))
            .filter(p => !beacons.includes(p));

        const solution = deduplicate(unbeacableCoords);
        return solution.length.toString();
    }

    public getFirstExpectedResult(): string {
        // RETURN EXPECTED SOLUTION FOR TEST 1;
        return '26';
    }

    public solveSecond(input: string): string {
        const sensorBalls: SensorBall[] = this.parseSensorBalls(input);
        const minCoord = 0;
        const maxCoord = 20; // TEST
        // const maxCoord = 4000000; // Actual solution

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

    private parseSensorBalls(input: string): SensorBall[] {
        return input.split('\n').map(line => {
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
}

