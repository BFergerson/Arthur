public class Functions {

    public void function1() {
        System.out.println("");
    }

    public void function2() {
        System.out.println("");
    }

    public void function3(int var) {
        System.out.println("intArg");
    }

    public void function4(int... var) {
        System.out.println("intVarargs");
    }

    public void function5(int var[]) {
        System.out.println("intArray1Arg");
    }

    public void function6(int[] var) {
        System.out.println("intArray2Arg");
    }

    public void function7(Integer[] var) {
        System.out.println("integerArrayArg");
    }

    public <T> void function8(T t) {
        System.out.println("genericsType1Arg");
    }

    public <T> void function9(List<T> var) {
        System.out.println("genericsType2Arg");
    }

    public void function10(List<Integer> var) {
        System.out.println("genericsIntegerArg");
    }
}