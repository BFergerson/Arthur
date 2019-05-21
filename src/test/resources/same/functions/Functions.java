public class Functions {

    public void function1() {
        System.out.println("");
    }

    public void function2() {
        System.out.println("");
    }

    public void function3(int var) {
        System.out.println("function3(int var)");
    }

    public void function4(int... var) {
        System.out.println("function4(int... var)");
    }

    public void function5(int var[]) {
        System.out.println("function5(int var[])");
    }

    public void function6(int[] var) {
        System.out.println("function6(int[] var)");
    }

    public void function7(Integer[] var) {
        System.out.println("function7(Integer[] var)");
    }

    public <T> void function8(T t) {
        System.out.println("function8(T t)");
    }

    public <T> void function9(List<T> var) {
        System.out.println("function9(List<T> var)");
    }

    public void function10(List<Integer> var) {
        System.out.println("function10(List<Integer> var)");
    }
}