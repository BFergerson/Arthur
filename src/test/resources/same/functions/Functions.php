<?php

function function1()
{
}

function function2()
{
}

function function3($param1, $param2)
{
    echo "withArgs";
}

function function4($param = 1)
{
    echo "defaultParam";
}

function function5($param = array("myArray"))
{
    echo "defaultArrayParam";
}

function function6($param = NULL)
{
    echo "defaultNullParam";
}

function function7(int $param)
{
    echo "typedIntArg";
}

function function8(&$param)
{
    echo "passByReference";
}

function function9($param1, ...$param2)
{
    echo "splatOperatorArg";
}

?>