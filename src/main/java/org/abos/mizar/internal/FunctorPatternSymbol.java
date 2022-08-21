package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record FunctorPatternSymbol(String name, List<String> prefixVariables, List<String> suffixVariables) implements FunctorPattern {

    public FunctorPatternSymbol(String name, List<String> prefixVariables, List<String> suffixVariables) {
        this.name = name;
        this.prefixVariables = Collections.unmodifiableList(prefixVariables);
        this.suffixVariables = Collections.unmodifiableList(suffixVariables);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        if (!environ.isValidSymbol(name, VocabularySymbols.O)) {
            throw new SyntaxException("*103 Unknown functor " + name);
        }
    }
}
