using System;

public class Operators
{
    public void andOperator()
    {
        if (true && true)
        {
            Console.WriteLine("");
        }
    }

    public void orOperator()
    {
        if (true || true)
        {
            Console.WriteLine("");
        }
    }

    public void isEqualOperator()
    {
        if (true == true)
        {
            Console.WriteLine("");
        }
    }

    public void isNotEqualOperator()
    {
        if (true != true)
        {
            Console.WriteLine("");
        }
    }

    public void ternaryOperator()
    {
        int a, b;
        a = 10;
        b = (a == 1) ? 20 : 30;
    }

    public void declareVariableOperator()
    {
        int x;
    }

    public void initializeVariableOperator()
    {
        int x = 0;
    }

    public void assignVariableOperator()
    {
        int x = 0;
        x = 1;
    }
}