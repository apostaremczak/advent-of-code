import { Coord, coordToStr, getManhattanDistance } from '../../utils/mazes';

export default class SensorBall {
    public readonly radius: number;

    constructor(readonly sensor: Coord, readonly closestBeacon: Coord) {
        this.radius = getManhattanDistance(sensor, closestBeacon);
    }

    public isWithinRadius(point: Coord) {
        return getManhattanDistance(this.sensor, point) <= this.radius;
    }

    public getUnbeacablePointsAt(y: number, xFrom: number, xTo: number): string[] {
        if (this.sensor.y + this.radius < y || this.sensor.y - this.radius > y) {
            return [];
        }
        const unbeacable: string[] = [];
        for (let x = xFrom; x <= xTo; x++) {
            const point: Coord = { x: x, y: y };
            if (this.isWithinRadius(point)) {
                unbeacable.push(coordToStr(point));
            }
        }
        return unbeacable;
    };

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
