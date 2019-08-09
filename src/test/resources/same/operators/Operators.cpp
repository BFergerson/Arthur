#include <iostream>

int main() {
}

void andOperator() {
    if (true && true) {
        std::cout << "and\n";
    }
}

void orOperator() {
    if (true || true) {
        std::cout << "or\n";
    }
}

void ternaryOperator() {
    int a, b;
    a = 10;
    b = (a == 1) ? 20 : 30;
}

void isEqualOperator() {
    if (true == true) {
        std::cout << "equal\n";
    }
}

void isNotEqualOperator() {
    if (true != true) {
        std::cout << "not equal\n";
    }
}

void declareVariableOperator() {
    int x;
}

void initializeVariableOperator() {
    int x = 0;
}

public void assignVariableOperator() {
    int x = 0;
    x = 1;
}