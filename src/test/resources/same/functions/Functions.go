package main

import "fmt"

type receiverType string

func function1() {
    fmt.Println("")
}

func function2() {
    fmt.Println("")
}

func function3(param uint64) {
    fmt.Println("uint64Arg")
}

func function4(param ...uint64) {
    fmt.Println("variadicFunc")
}

func function5(param [3]uint64) {
    fmt.Println("uint64ArrayArg")
}

func function6(param []uint64) {
    fmt.Println("uint64SliceArg")
}

func Function7(param uint64) {
    fmt.Println("exportedFunc")
}

func (receiverType) function8(param uint64) {
    fmt.Println("asMethodWithReceiver1")
}

func (t receiverType) function9(param uint64) {
    fmt.Println("asMethodWithReceiver2")
}

func function10(param *uint64) {
    fmt.Println("pointerArg")
}

func (*receiverType) function11(param uint64) {
    fmt.Println("asMethodWithPointerReceiverArg1")
}

func (t *receiverType) function12(param uint64) {
    fmt.Println("asMethodWithPointerReceiverArg2")
}