#!/bin/bash

ifConditional() {
  if [ true && true ]; then
    echo ""
  fi
}

ifElseConditional() {
  if [ true && true ]; then
    echo ""
  else
    echo ""
  fi
}

ifElseIfConditional() {
  if [ true && true ]; then
    echo ""
  elif [ true && true ]; then
    echo ""
  fi
}

switchConditional() {
  case $param in
  esac
}

switchCaseConditional() {
  case $param in
    1)
      echo ""
      ;;
  esac
}