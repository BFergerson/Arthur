import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.util.List;

public class ImportQualifiedName {
    public static void acceptGuavaSet(ImmutableSet<Boolean> arg) {
        arg.hashCode();
    }

    public static void acceptGuavaMap(ImmutableMap<Boolean, Integer> arg) {
        arg.hashCode();
    }

    public static void innerVariable() {
        List arrayList = Lists.newArrayList();
    }
}
