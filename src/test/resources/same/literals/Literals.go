package main

import "fmt"

func main() {
    param1 := 1
    param2 := "stringParam2"
    param3 := 0xFFFFFFFFFFFF
    var param4 uint64 = 0xff1a618b7f65ea12
    var param5 uint64 = 0xc4ceb9fe1a85ec53
    param6 := 0757
    param7 := 0123546263753256452432
    param8 := 123.E+2
    param9 := 1e-1
    param10 := -1e-1
    param11 := 1.e+0i

    fmt.Println(param1,param2,param3,param4,param5)
    fmt.Println(param6,param7,param8,param9,param10)
    fmt.Println(param11)
}
