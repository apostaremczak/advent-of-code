export default class File {
    public absolutePath: string;
    constructor(readonly dir: string, readonly fileName: string, readonly size: number) {
        if (dir === '/') {
            this.absolutePath = `/${fileName}`;
        } else {
            this.absolutePath = `${dir}/${fileName}`;
        }
    }
}
