package main

import "fmt"

type receiverType string

func function1() {
    fmt.Println("")
}

func function2() {
    fmt.Println("")
}

func function3(v uint64) {
    fmt.Println("function3(v uint64)")
}

func function4(v ...uint64) {
    fmt.Println("function4(v ...uint64)")
}

func function5(v [3]uint64) {
    fmt.Println("function5(v [3]uint64)")
}

func function6(v []uint64) {
    fmt.Println("function6(v []uint64)")
}

func Function7(v uint64) {
    fmt.Println("Function7(v uint64)")
}

func (receiverType) function8(v uint64) {
    fmt.Println("function8(v uint64)")
}

func (t receiverType) function9(v uint64) {
    fmt.Println("function9(v uint64)")
}

func function10(v *uint64) {
    fmt.Println("function10(v *uint64)")
}

func (*receiverType) function11(v uint64) {
    fmt.Println("function11(v uint64)")
}

func (t *receiverType) function12(v uint64) {
    fmt.Println("function12(v uint64)")
}