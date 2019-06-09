public class Functions {

    public void function1() {
        System.out.println("");
    }

    public void function2() {
        System.out.println("");
    }

    public void function3(int param1, String param2) {
        System.out.println("withArgs");
    }

    public void function4(int... param) {
        System.out.println("intVarargs");
    }

    public void function5(int param[]) {
        System.out.println("intArray1Arg");
    }

    public void function6(int[] param) {
        System.out.println("intArray2Arg");
    }

    public void function7(Integer[] param) {
        System.out.println("integerArrayArg");
    }

    public <T> void function8(T t) {
        System.out.println("genericsType1Arg");
    }

    public <T> void function9(List<T> param) {
        System.out.println("genericsType2Arg");
    }

    public void function10(List<Integer> param) {
        System.out.println("genericsIntegerArg");
    }
}