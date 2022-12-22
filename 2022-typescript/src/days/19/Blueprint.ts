import { findAllGroups } from '../../utils/strings';
import State from './State';

const BlueprintRegex = /Blueprint (\d+): Each ore robot costs (\d+) ore\. Each clay robot costs (\d+) ore\. Each obsidian robot costs (\d+) ore and (\d+) clay\. Each geode robot costs (\d+) ore and (\d+) obsidian\./g;

export type Cost = {
    ore: number,
    clay: number,
    obsidian: number
}

export default class Blueprint {
    public maxOreCost: number;
    public maxClayCost: number;
    public maxObsidianCost: number;

    constructor(
        readonly index: number,
        readonly oreRobotCost: Cost,
        readonly clayRobotCost: Cost,
        readonly obsidianRobotCost: Cost,
        readonly geodeRobotCost: Cost,
        readonly totalMinutes: number
    ) {
        this.maxOreCost = Math.max(...[oreRobotCost.ore, clayRobotCost.ore, obsidianRobotCost.ore, geodeRobotCost.ore]);
        this.maxClayCost = obsidianRobotCost.clay;
        this.maxObsidianCost = geodeRobotCost.obsidian;
    }

    private canAfford(state: State, cost: Cost): boolean {
        return state.oreResources >= cost.ore
            && state.clayResources >= cost.clay
            && state.obsidianResources >= cost.obsidian;
    }

    public canBuildOreRobot(state: State): boolean {
        return this.canAfford(state, this.oreRobotCost);
    }

    public canBuildClayRobot(state: State): boolean {
        return this.canAfford(state, this.clayRobotCost);
    }

    public canBuildObsidianRobot(state: State): boolean {
        return this.canAfford(state, this.obsidianRobotCost);
    }

    public canBuildGeodeRobot(state: State): boolean {
        return this.canAfford(state, this.geodeRobotCost);
    }

    public findMaxGeodes(): number {
        // First minute - empty state
        let queue = [new State(this)];
        // Map: state hash, number of minutes left when visited that state
        const visitedBefore = new Map<string, number>();
        let bestGeodeScore = 0;

        while (queue.length > 0) {
            const state = queue.pop();

            // If we investigated this state before, then only reconsider this branch if we have more minutes left
            const stateHash = state.getHash();
            const prevMinutesLeft = visitedBefore.get(stateHash) || -Infinity;
            // console.log(prevMinutesLeft);
            if (state.minutesLeft > prevMinutesLeft) {
                // Label the current state as examined
                visitedBefore.set(stateHash, state.minutesLeft);
                // Only consider this branch if it could potentially produce a better geode result
                if (state.getMaximumPossibleGeodeResult() > bestGeodeScore) {
                    // console.log(`Adding a new state to the queue`);
                    queue = queue.concat(state.getNextPossibleStates());
                }
            }

            // If this is the last minute, save the result
            if (state.minute === this.totalMinutes) {
                if (state.geodeResources > bestGeodeScore) {
                    bestGeodeScore = state.geodeResources;
                }
            }
        }

        console.log(`[MAX GEODE SCORE] ID: ${this.index} -> ${bestGeodeScore}`);
        return bestGeodeScore;
    }

    public getQualityLevel(): number {
        const res = this.findMaxGeodes();
        console.log(`[QUALITY LEVEL] ID: ${this.index} -> ${res}`);
        return res * this.index;
    }
}

export function parseBlueprints(input: string, totalMinutes = 24): Blueprint[] {
    return input.split('\n').map(line => {
        const matches = findAllGroups(line, BlueprintRegex);
        return new Blueprint(
            Number(matches[1]),
            { ore: Number(matches[2]), clay: 0, obsidian: 0 },
            { ore: Number(matches[3]), clay: 0, obsidian: 0 },
            { ore: Number(matches[4]), clay: Number(matches[5]), obsidian: 0 },
            { ore: Number(matches[6]), clay: 0, obsidian: Number(matches[7]) },
            totalMinutes
        );
    });
}
