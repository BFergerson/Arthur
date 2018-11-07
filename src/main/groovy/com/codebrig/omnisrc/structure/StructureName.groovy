package com.codebrig.omnisrc.structure

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.structure.name.JavaName

class StructureName {

    static String getNodeName(SourceNode node) {
        switch (node.language) {
            case SourceLanguage.Java:
                return JavaName.getNodeName(node)
            default:
                throw new IllegalArgumentException("Unsupported language: " + node.language)
        }
    }
}
