import Blueprint, { Cost } from './Blueprint';

export default class State {
    public minutesLeft: number;
    constructor(
        readonly blueprint: Blueprint,
        readonly minute: number = 0,
        readonly oreRobotsCount: number = 1,
        readonly oreResources: number = 0,
        readonly clayRobotsCount: number = 0,
        readonly clayResources: number = 0,
        readonly obsidianRobotsCount: number = 0,
        readonly obsidianResources: number = 0,
        readonly geodeRobotsCount: number = 0,
        readonly geodeResources: number = 0
    ) {
        this.minutesLeft = this.blueprint.totalMinutes - minute;
    }

    public getNextPossibleStates(): State[] {
        const nextStates: State[] = [];

        // Return an empty list if the time has run out
        if (this.minute == this.blueprint.totalMinutes) {
            return nextStates;
        }

        // Build geode robots whenever possible
        if (this.blueprint.canBuildGeodeRobot(this)) {
            nextStates.push(this.generateNewState(
                this.blueprint.geodeRobotCost,
                false,
                false,
                false,
                true
            ));
        } else {
            // Build other robots when possible, but do not build more than the maximum cost for this resource
            // We can only build one robot per minute, so building more robots than this cost would end up
            // in a wasted surplus of resources
            const canBuildObsidian = this.blueprint.canBuildObsidianRobot(this)
                && this.obsidianRobotsCount < this.blueprint.maxObsidianCost;
            // Do not build it if all necessary obsidian robots are built already
            const canBuildClay = this.blueprint.canBuildClayRobot(this)
                && this.clayRobotsCount < this.blueprint.maxClayCost
                && this.obsidianRobotsCount <= this.blueprint.maxObsidianCost;
            const canBuildOre = this.blueprint.canBuildOreRobot(this)
                && this.oreRobotsCount < this.blueprint.maxOreCost;
            const canBuildAll = canBuildObsidian && canBuildClay && canBuildOre;

            if (canBuildObsidian) {
                nextStates.push(this.generateNewState(
                    this.blueprint.obsidianRobotCost,
                    false,
                    false,
                    true
                ));
            }
            if (canBuildClay) {
                nextStates.push(this.generateNewState(
                    this.blueprint.clayRobotCost,
                    false,
                    true
                ));
            }
            if (canBuildOre) {
                nextStates.push(this.generateNewState(
                    this.blueprint.oreRobotCost,
                    true
                ));
            }

            // Do nothing, simply produce resources
            // Doing anything is still better than waiting, so skip this if you can build all the other robots
            if (!canBuildAll) {
                nextStates.push(this.generateNewState());
            }
        }

        return nextStates;
    }

    private generateNewState(
        cost?: Cost,
        addOreRobot = false,
        addClayRobot = false,
        addObsidianRobot = false,
        addGeodeRobot = false
    ): State {
        const oreResources = this.oreResources + this.oreRobotsCount - (cost?.ore || 0);
        const clayResources = this.clayResources + this.clayRobotsCount - (cost?.clay || 0);
        const obsidianResources = this.obsidianResources + this.obsidianRobotsCount - (cost?.obsidian || 0);
        const geodeResources = this.geodeResources + this.geodeRobotsCount;

        return new State(
            this.blueprint,
            this.minute + 1,
            this.oreRobotsCount + (addOreRobot ? 1 : 0),
            oreResources,
            this.clayRobotsCount + (addClayRobot ? 1 : 0),
            clayResources,
            this.obsidianRobotsCount + (addObsidianRobot ? 1 : 0),
            obsidianResources,
            this.geodeRobotsCount + (addGeodeRobot ? 1 : 0),
            geodeResources
        );
    }

    public getHash(): string {
        return JSON.stringify({
            oreRobotsCount: this.oreRobotsCount,
            oreResources: this.oreResources,
            clayRobotsCount: this.clayRobotsCount,
            clayResources: this.clayResources,
            obsidianRobotsCount: this.obsidianRobotsCount,
            obsidianResources: this.obsidianResources,
            geodeRobotsCount: this.geodeRobotsCount,
            geodeResources: this.geodeResources
        });
    }

    public getMaximumPossibleGeodeResult(): number {
        // Assuming we can afford to build a geode robot every single minute until we run out of time,
        // what could be the maximum result of this state?

        // Number of elements to be summed
        const n = this.minutesLeft + 1;
        // Resources currently produced every minute
        const a = this.geodeRobotsCount;
        // Resources produced in the last minute, assuming we build a robot every minute
        const l = this.minutesLeft + this.geodeRobotsCount;
        // Sum of integers within a range
        const increase = (n * (a + l)) / 2;

        return this.geodeResources + increase;
    }

}
