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