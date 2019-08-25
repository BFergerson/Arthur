#!/bin/bash

andOperator() {
  if true && true; then
     echo ""
  fi
}

orOperator() {
  if true || true; then
     echo ""
  fi
}

isEqualOperator1() {
  if $1 == "a"; then
     echo ""
  fi
}

isEqualOperator2() {
  if $1 -eq "a"; then
     echo ""
  fi
}

isNotEqualOperator1() {
  if $1 != "a"; then
     echo ""
  fi
}

isNotEqualOperator2() {
  if $1 -ne "a"; then
     echo ""
  fi
}