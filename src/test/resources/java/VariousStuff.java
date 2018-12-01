import java.util.List;
import java.util.Map;

class VariousStuff {

    public int var;
    public static int var2;
    public final static int var3 = 1;
    private static int var4 = 1;

    public void method_QualifiedTypeArgs(TypeToken<?>.TypeSet args) {
    }

//    public void method_ObjectVarArgs(Object... args) {
//    }

    public void method_MapEntryArrayArgs(Map.Entry<Object, String>[] args) {
    }

    void method_StringArrayArgs(String[] args) {
    }

    void method_StringArrayArgs2(String[][] args) {
    }

    void method_IterableArgs(Iterable<?> args) {
    }

    void method_ListMapArgs(List<Map.Entry<?, ?>> args) {
    }

    int method_3_args(String s, int x, Object o) {
        return 1;
    }

    int method_4_args(byte b, String s, int x, Object o) {
        return 1;
    }

    void method_5_args(byte b, String s, int x, Object o, char c) {
        //System.out.println("hello");
    }

    public class TypeToken<T> {
        public class TypeSet {
        }
    }
}
