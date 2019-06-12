package com.codebrig.arthur.observe.structure.naming.util

/**
 * Various common utility methods for AST naming.
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class NamingUtils {

    static String trimTrailingComma(String name) {
        if (name.endsWith(",")) {
            name = name.substring(0, name.length() - 1)
        }
        return name
    }
}
