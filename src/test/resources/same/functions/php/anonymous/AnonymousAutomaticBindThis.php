<?php

class Test
{
    public function testing()
    {
        return function() {
            var_dump($this);
        };
    }
}

?>