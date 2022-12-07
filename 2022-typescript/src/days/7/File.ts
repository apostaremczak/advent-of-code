import { SystemObject } from './SystemObject';

export default class File implements SystemObject {
    constructor(readonly name: string, readonly size: number) {
    }

    public getSize(): number {
        return this.size;
    }

    public isDirectory(): boolean {
        return false;
    }
}
