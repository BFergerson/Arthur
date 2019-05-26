function function1() {
    console.log("");
}

function function2() {
    console.log("");
}

function function3(param) {
    console.log("withArg");
}

var function4Var = function() {
    console.log("anonymousNoArg");
};

var function5Var = function(param) {
    console.log("anonymousWithArg");
};

var function6Var = function function6() {
    console.log("namedNoArg");
};

var function7Var = function function7(param) {
    console.log("namedWithArg");
};

var function8Var = () => {
    console.log("anonymousArrowNoArg");
};

var function9Var = (param) => {
    console.log("anonymousArrowWithArg");
};

var function11Var = 'function11';
var function12Var = 'function12';
var object1Var = {
    function10() {
        console.log("shorthandNoArg");
    },
    [function11Var]() {
        console.log("computedNoArg");
    },
    [function12Var](param) {
        console.log("computedWithArg");
    }
};

function* function13() {
    console.log("generatorDeclarationNoArg");
};

function* function14(param) {
    console.log("generatorDeclarationWithArg");
};

var function15Var = function*() {
    console.log("generatorExpressionAnonymousNoArg");
};

var function16Var = function*(param) {
    console.log("generatorExpressionAnonymousWithArg");
};

var function17Var = function* function17() {
    console.log("generatorExpressionNamedNoArg");
};

var function18Var = function* function18(param) {
    console.log("generatorExpressionNamedWithArg");
};

var object2Var = {
    *function19() {
        console.log("generatorShorthandNoArg");
    },
    *function20(param) {
        console.log("generatorShorthandWithArg");
    }
};

var number1 = 'number1';
var number2 = 'number2';
var newFunctionVar = new Function(number1, number2,
    'return number1 + number2'
);

(function() {
    console.log("IIFE");
})();

function function21(param = 1) {
    console.log("defaultParam");
}