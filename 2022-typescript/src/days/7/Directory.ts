import { SystemObject } from './SystemObject';
import File from './File';
import { sum } from '../../utils/arrays';

export default class Directory implements SystemObject {

    protected children: Directory[];
    protected files: File[];
    protected cachedSize = 0;

    constructor(readonly name: string, readonly parent?: Directory) {
        this.children = [];
        this.files = [];
    }

    public addSubdirectory(child: Directory) {
        if (!this.children.includes(child)) {
            this.children.push(child);
        }
    }

    public addFile(file: File) {
        if (!this.files.includes(file)) {
            this.files.push(file);
            this.cachedSize += file.size;
            this.parent?.updateCachedSize(file.size);
        }
    }

    protected updateCachedSize(size: number): void {
        this.cachedSize += size;
        this.parent?.updateCachedSize(size);
    }

    public getParent() {
        return this.parent;
    }

    private getFilesSize(): number {
        return sum(this.files.map(f => f.getSize()));
    }

    private getChildrenSize(): number {
        return sum(this.children.map(d => d.getSize()));
    }

    public getSize(): number {
        return this.cachedSize;
    }

    public isDirectory(): boolean {
        return true;
    }
}
