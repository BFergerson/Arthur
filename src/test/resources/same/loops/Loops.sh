#!/bin/bash

forLoop() {
  for (( i = 0; i < 10; i++ ))
  do
     echo $i
  done
}

forEachLoop() {
  for num in {1..5}
  do
    echo $num
  done
}

whileLoop() {
  while true
  do
     echo ""
  done
}
