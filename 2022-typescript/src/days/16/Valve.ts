import { findAllGroups } from '../../utils/strings';


export default class Valve {
    constructor(readonly name: string, readonly flowRate: number, readonly connectedTo: string[]) {
    }
}

export class ValveFactory {
    private ValveRegex = /Valve (\S+) has flow rate=(\d+); tunnels* leads* to valves* (.+)/g;

    public getValve(inputLine: string): Valve {
        const matches = findAllGroups(inputLine, this.ValveRegex);
        const name = matches[1];
        const flowRate = Number(matches[2]);
        const neighborNames = matches[3].split(', ');
        return new Valve(name, flowRate, neighborNames);
    }
}
