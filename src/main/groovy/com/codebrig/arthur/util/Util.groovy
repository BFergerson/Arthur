package com.codebrig.arthur.util

class Util {

    static String trimTrailingComma(String name) {
        if (name.endsWith(",")) {
            name = name.substring(0, name.length() - 1)
        }
        return name
    }
}
