import { Coord, coordToStr, getManhattanDistance } from '../../utils/mazes';
import { rangeInclusive, sum } from '../../utils/arrays';

export default class SensorBall {
    public readonly radius: number;

    constructor(readonly sensor: Coord, readonly closestBeacon: Coord) {
        this.radius = getManhattanDistance(sensor, closestBeacon);
    }

    public isWithinRadius(point: Coord) {
        return getManhattanDistance(this.sensor, point) <= this.radius;
    }

    public getCoveredRangeAt(y: number): { start: number, stop: number } | null {
        // Only consider the balls that can touch the y-line
        if (this.sensor.y + this.radius < y || this.sensor.y - this.radius > y) {
            return null;
        }
        // y-line crosses the ball
        // Calculate the distance from the center of the ball to the y line
        const a = Math.abs(this.sensor.y - y);
        // What is the remaining radius length at the disposal on each side (with regard to the center)?
        const b = this.radius - a;
        return {
            start: this.sensor.x - b,
            stop: this.sensor.x + b
        };
    }

    public getOutsideBorder(): Coord[] {
        const top = {
            x: this.sensor.x,
            y: this.sensor.y + this.radius + 1
        };
        const bottom = {
            x: this.sensor.x,
            y: this.sensor.y - this.radius - 1
        };
        const left = {
            x: this.sensor.x - this.radius - 1,
            y: this.sensor.y
        };
        const right = {
            x: this.sensor.x + this.radius + 1,
            y: this.sensor.y
        };


        // Go from the top to the right corner
        const border: Coord[] = [];
        let current = top;
        while (JSON.stringify(current) !== JSON.stringify(right)) {
            border.push(current);
            current = {
                x: current.x + 1,
                y: current.y - 1
            };
        }
        // Go from the right to the bottom corner
        while (JSON.stringify(current) !== JSON.stringify(bottom)) {

            border.push(current);
            current = {
                x: current.x - 1,
                y: current.y - 1
            };
        }
        // Go from the bottom to the left corner
        while (JSON.stringify(current) !== JSON.stringify(left)) {
            border.push(current);
            current = {
                x: current.x - 1,
                y: current.y + 1
            };
        }
        // Go from the left to the top corner
        while (JSON.stringify(current) !== JSON.stringify(top)) {
            border.push(current);
            current = {
                x: current.x + 1,
                y: current.y + 1
            };
        }

        return border;
    }
}
