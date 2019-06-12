function function1() {
    console.log("");
}

function function2() {
    console.log("");
}

function function3(param1, param2) {
    console.log("withArgs");
}

function* function4() {
    console.log("generatorDeclarationNoArg");
};

function* function5(param) {
    console.log("generatorDeclarationWithArg");
};

function function6(param = 1) {
    console.log("defaultParam");
}

function function7(param1, ...param2) {
    console.log("restOperatorParam");
}

function function8(param1: any, param2: number) {
    console.log("withTypedArg");
}