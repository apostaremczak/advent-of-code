// Since this is my very first contact with TypeScript, here's a basic syntax overview
// Based on this tutorial: https://youtu.be/d56mG7DezGs

// Basic types
const sales = 123_456_789;
const course = 'cs';
const is_published = true;

// If I don't specify the type, TS will infere the type by the declared value
const other_sales = 123;

function render(document: any): void {
    console.log(document);
}

// Arrays
const nums: number[] = [1, 2, 3];
const nums_ins = nums.map(n => n + 3);
render(nums_ins);

// Tuple declaration
// Tuples are compiled to simple JS arrays
const user: [number, string] = [1, 'Adzia'];

// Enumerations
// By default, the first member is assigned the value of 0, the 2nd gets 1, etc.
enum Size { Small, Medium, Large };
// I can override those default values with anything I want.
// They can also be other types than just numbers.
enum SizeAnnotated { Small = 1, Medium = 2, Large = 3 };

const mySize: Size = Size.Medium;
render(mySize);

// If I add 'const' before an enumeration, the compiled code will be optimized
const enum OptSize { Small = 'S', Medium = 'M', Large = 'L' };
const myOptSize = OptSize.Large;

// Function declaration
// taxYear is an optional argument
function calculateTax(income: number, taxYear?: number): number {
    if (income <= 50_000)
        // Default value of an option:  (optional_variable || defaul_value)
    {
        if ((taxYear || 2022) < 2022) {
            return 1.2 * income;
        } else {
            return 1.17 * income;
        }
    }
    return 1.32 * income;
}

// Object initialization
// By default, all those parameters are mutable
const employee: {
    id: number,
    name?: string
} = { id: 1 };
employee.name = 'xd';

const constEmployee: {
    // Setting a field to be unmutable -> readonly
    readonly id: number,
    // Method declaration
    retire: (date: Date) => void
} = {
    id: 234,
    retire: (date: Date) => {
        render(date);
    }
};

// Advanced types - type aliases
type Employee = {
    readonly id: number,
    name: string,
    retire: (date: Date) => void
}

const newEmployee: Employee = {
    id: 234,
    name: 'Adam',
    retire: (date: Date) => {
        render(date);
    }
};

// Union types: firstType | secondType
function kgToLbs(weight: number | string): number {
    // Narrowing - matching a specific type
    if (typeof weight === 'number') {
        return weight * 2.2;
    } else {
        return (kgToLbs(parseInt(weight)));
    }
};

// Type intersection: firstType & secondType;
type Draggable = {
    drag: () => void
};

type Resizeable = {
    resize: () => void
};

type UIWidget = Draggable & Resizeable;

// Sidenote:
// If I don't put in semicolons in some obvious places, TS will probably insert them for me
// The best practice is still to put it semicolons
const textBox: UIWidget = {
    drag: () => {
    },
    resize: () => {
    }
};

// Literal types
// Used when we want to limit the possible values assigned to an object
// Literal (exact, specific)
const quantity: 50 | 100 | 150 = 100;
type Quantity = 50 | 100;
const newQuant: Quantity = 50;

type Metric = 'cm' | 'inch';

// Nullable types
function greet(name: string) {
    console.log(name.toUpperCase);
}

// Normally I cannot call greet(null) in TS
// I could turn it off by turning off 'strictNullChecks'

function nullableGreet(name: string | null | undefined) {
    if (name) {
        const greeting = `Ciao, ${name}!`;
        render(greeting);
    } else
        // This is invoked when name is of type null
    {
        render('Ciao!');
    }
}

nullableGreet(null);
nullableGreet('Sara');
nullableGreet(undefined);

// Optional chaining
type Customer = {
    birthday: Date
};

function getCustomer(id: number): Customer | null | undefined {
    // If there id is equal to zero, then return null
    // Otherwise create a new object with some random data
    return id === 0 ? null : { birthday: new Date() };
}

const customer = getCustomer(0);

// We can replace these lines:
// if (customer !== null && customer !== undefined) {
//     render(customer.birthday);
// }
// With this, the optional property access operator:
render(customer?.birthday);
// The line above will only get executed if the customer is neither null nor undefined
// Otherwise the result of this expression will be 'undefined'

// Optional element access operator
// Accessing elements of possibly empty arrays
// customers?.[0]

// Optional call
const log: any = (m: string) => render(m);
const nullLog: any = null;
nullLog?.('a');


// Reading files

import { readFileSync, promises as fsPromises } from 'fs';
import { join } from 'path';

// ✅ read file SYNCHRONOUSLY
function syncReadFile(filename: string) {
    const result = readFileSync(join(__dirname, filename), 'utf-8');

    console.log(result);
    return result;
}

syncReadFile('00.txt');

// --------------------------------------------------------------

// ✅ read file ASYNCHRONOUSLY
async function asyncReadFile(filename: string) {
    try {
        const result = await fsPromises.readFile(
            join(__dirname, filename),
            'utf-8',
        );

        console.log(result);

        return result;
    } catch (err) {
        console.log(err);
        return 'Something went wrong';
    }
}

asyncReadFile('00.txt');

