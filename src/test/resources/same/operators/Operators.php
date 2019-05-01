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

function equalOperator()
{
    if (true == true) {
        echo "";
    }
}

function notEqualOperator()
{
    if (true != true) {
        echo "";
    }
}

function equalTypeOperator()
{
    if (true === true) {
        echo "";
    }
}

function notEqualTypeOperator()
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

?>