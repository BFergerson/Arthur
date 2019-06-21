package main

import "fmt"

func andOperator() {
	if true && true {
		fmt.Println("")
	}
}

func orOperator() {
	if true || true {
		fmt.Println("")
	}
}

func isEqualOperator() {
	if true == true {
		fmt.Println("")
	}
}

func isNotEqualOperator() {
	if true != true {
		fmt.Println("")
	}
}

func declareVariableOperator() {
	var x int
}

func initializeVariableOperator() {
	var x int = 0
}

func assignVariableOperator() {
	var x int = 0
	x = 1
}
