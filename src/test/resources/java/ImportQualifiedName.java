import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class ImportQualifiedName {
    public static void acceptGuavaSet(ImmutableSet<Boolean> arg) {
        arg.hashCode();
    }

    public static void acceptGuavaMap(ImmutableMap<Boolean, Integer> arg) {
        arg.hashCode();
    }
}
