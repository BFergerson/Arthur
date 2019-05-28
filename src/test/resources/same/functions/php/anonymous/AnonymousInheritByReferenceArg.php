<?php

$param = 'param';
$functionVar = function () use (&$param)
{
    echo "anonymousInheritByReferenceArg";
};

?>