function function1() {
    console.log("");
}

function function2() {
    console.log("");
}

function function3(param) {
    console.log("withArg");
}

var function4Var = function function4() {
    console.log("namedNoArg");
};

var function5Var = function function5(param) {
    console.log("namedWithArg");
};

var function7Var = 'function7';
var function8Var = 'function8';
var object1Var = {
    function6() {
        console.log("shorthandNoArg");
    },
    [function7Var]() {
        console.log("computedNoArg");
    },
    [function8Var](param) {
        console.log("computedWithArg");
    }
};

function* function9() {
    console.log("generatorDeclarationNoArg");
};

function* function10(param) {
    console.log("generatorDeclarationWithArg");
};

var function11Var = function* function11() {
    console.log("generatorExpressionNamedNoArg");
};

var function12Var = function* function12(param) {
    console.log("generatorExpressionNamedWithArg");
};

var object2Var = {
    *function13() {
        console.log("generatorShorthandNoArg");
    },
    *function14(param) {
        console.log("generatorShorthandWithArg");
    }
};

var number1 = 'number1';
var number2 = 'number2';
var newFunctionVar = new Function(number1, number2,
    'return number1 + number2'
);

function function15(param = 1) {
    console.log("defaultParam");
}