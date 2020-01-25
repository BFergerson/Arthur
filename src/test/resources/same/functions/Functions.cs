using System;
using System.Collections.Generic;

public class Functions {

    public void Function1() {
        Console.WriteLine("noArgs");
    }

    public void Function2(int param) {
        Console.WriteLine("withIntArg");
    }

    public void Function3(int param1, string param2) {
        Console.WriteLine("withAgs");
    }

    public void Function4(params int[] param) {
        Console.WriteLine("intVarargs");
    }

    public void Function5(int[] param) {
        Console.WriteLine("intArrayArg");
    }

    public void Function6(sbyte param1, short param2, long param3) {
        Console.WriteLine("signedArgs");
    }

    public void Function7(byte param1, ushort param2, uint param3, ulong param4) {
        Console.WriteLine("unsignedArgs");
    }

    public void Function8(bool param1, decimal param2, float param3, double param4) {
        Console.WriteLine("restOfSimpleArgs");
    }

    public void Function9(MyStruct param) {
        Console.WriteLine("structArg");
    }

    enum Day { Sun, Mon, Tue, Wed, Thu, Fri, Sat };
    public void Function10(Day param) {
        Console.WriteLine("enumArg");
    }

    public void Function11(T t) {
        Console.WriteLine("genericsType1Arg");
    }

    public void Function12(List<T> param) {
        Console.WriteLine("genericsType2Arg");
    }

    public void Function13(List<Day> param) {
        Console.WriteLine("genericsEnumArg");
    }

    public void Function14(List<MyStruct> param) {
        Console.WriteLine("genericsStructArg");
    }

    public void Function15(List<MyStruct> param1, params int[] param2) {
        Console.WriteLine("genericsStructArgAndIntVarargs");
    }
}

public struct MyStruct
{
    public string name;
}