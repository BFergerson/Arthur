var param1 = 1;
const param2 = 2;
let param3 = 3;
let param4 = "stringParam4";

// Strict mode in ECMAScript 5 forbids octal syntax.
// Octal syntax isn't part of ECMAScript 5, but it's
// supported in all browsers by prefixing the octal
// number with a zero.
let param5 = 0777;
let param6 = 0888;

// ES6+
let param7 = 0o10;
let param8 = -0o10;

let param9 = 0b01111111100000000000000000000000;
let param10 = 0B00000000011111111111111111111111;
let param11 = 0x123456789ABCDEF;
let param12 = 0xFFFFFFFFFFFFFFFF;
let param13 = -1.2e55;
let param14 = 1.2e-55;
let param15 = -1.2e-55;
let param16 = .1E3;