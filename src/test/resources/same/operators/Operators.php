<?php

function andOperator()
{
    if (true && true) {
        echo "";
    }
}

function orOperator()
{
    if (true || true) {
        echo "";
    }
}

function isEqualOperator()
{
    if (true == true) {
        echo "";
    }
}

function isNotEqualOperator()
{
    if (true != true) {
        echo "";
    }
}

function isEqualTypeOperator()
{
    if (true === true) {
        echo "";
    }
}

function isNotEqualTypeOperator()
{
    if (true !== true) {
        echo "";
    }
}

function ternaryOperator()
{
    $a = 10;
    $b = ($a == 1) ? 20 : 30;
}

function declareVariableOperator()
{
    $x = 0;
}

function initializeVariableOperator()
{
    $x = 0;
}

function assignVariableOperator()
{
    $x = 0;
    $x = 1;
}

?>