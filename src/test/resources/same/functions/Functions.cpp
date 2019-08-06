#include <iostream>
#include <vector>
#include <initializer_list>
#include <string>
#include <list>

struct myStruct {
    string name;
}

void function1() {
  std::cout << "noArgs\n";
}

void function2(int param) {
  std::cout << "withIntArg\n";
}

void function3(int param1, int param2) {
  std::cout << "withArgs\n";
}

void function4(int param...) {
  std::cout << "withVarargs1\n";
}

void function5(int param, ...) {
  std::cout << "withVarargs2\n";
}

void function6(std::vector<int> param) {
  std::cout << "withVarargs3\n";
}

void function7(std::initializer_list<int> param) {
  std::cout << "withVarargs4\n";
}

void function8(int param1, string param2, std::vector<int> param3) {
  std::cout << "withVarargs5\n";
}

void function9(int param[]) {
  std::cout << "withArrayArg1\n";
}

void function10(int param[3]) {
  std::cout << "withArrayArg2\n";
}

void function11(int param1[3], string param2) {
  std::cout << "withArrayArg3\n";
}

void function12(string param1, int param2[], int param3) {
  std::cout << "withArrayArg4\n";
}

template <class T>
T function13(T t) {
  std::cout << "withTemplateArg\n";
  return t;
}

void function14(list<T> param) {
  std::cout << "withTemplateListArg1\n";
}

void function15(string param1, list<T> param2) {
  std::cout << "withTemplateListArg2\n";
}

void function16(myStruct param) {
  std::cout << "withStructArg\n";
}