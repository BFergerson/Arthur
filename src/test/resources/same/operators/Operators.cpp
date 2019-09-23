#include <iostream>

void andOperator() {
    if (true && true) {
        std::cout << "";
    }
}

void orOperator() {
    if (true || true) {
        std::cout << "";
    }
}

void ternaryOperator() {
    int a, b;
    a = 10;
    b = (a == 1) ? 20 : 30;
}

void isEqualOperator() {
    if (true == true) {
        std::cout << "";
    }
}

void isNotEqualOperator() {
    if (true != true) {
        std::cout << "";
    }
}

void declareVariableOperator() {
    int x;
}

void initializeVariableOperator() {
    int x = 0;
}

void assignVariableOperator() {
    int x = 0;
    x = 1;
}
