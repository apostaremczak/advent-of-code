type PacketValue = number | Packet;

export type Packet = Array<PacketValue>;

export type PacketData = [left: Packet, right: Packet]

function areBothNumbers(left: PacketValue, right: PacketValue): boolean {
    return Number.isInteger(left) && Number.isInteger(right);
}

function areBothArrays(left: PacketValue, right: PacketValue): boolean {
    return Array.isArray(left) && Array.isArray(right);
}

// Converts a packet value to a packet (if not one already)
function convertToPacket(value: PacketValue): Packet {
    return typeof value === 'number' ? [value] : value;
};

export function areInCorrectOrder(left: Packet, right: Packet): boolean | null {
    if (areBothNumbers(left, right)) {
        return left < right;
    } else if (areBothArrays(left, right)) {

        // Compare two arrays element by element
        for (const [index, leftEntry] of left.entries()) {
            const rightEntry = right[index];
            if (rightEntry === undefined) {
                // The right side has run out of packets - the inputs are not in the right order
                return false;
            }
            if (areBothNumbers(leftEntry, rightEntry)) {
                // Continue the loop if both numbers are the same
                if (leftEntry === rightEntry) {
                    continue;
                } else {
                    return leftEntry < rightEntry;
                }
            }
            const comparison = areInCorrectOrder(convertToPacket(leftEntry), convertToPacket(rightEntry));
            if (comparison !== null) {
                return comparison;
            }
        }

        // Either the left side run out of items or no comparison could be found
        return left.length < right.length ? true : null;
    } else if (Array.isArray(left)) {
        return areInCorrectOrder(left, [right]);
    } else {
        return areInCorrectOrder([left], right);
    }
}
