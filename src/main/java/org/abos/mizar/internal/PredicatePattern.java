package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record PredicatePattern(String name, List<String> prefixVariables, List<String> suffixVariables) implements Syntax {

    public PredicatePattern(String name, List<String> prefixVariables, List<String> suffixVariables) {
        this.name = name;
        this.prefixVariables = Collections.unmodifiableList(prefixVariables);
        this.suffixVariables = Collections.unmodifiableList(suffixVariables);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        if (!environ.isValidSymbol(name, VocabularySymbols.R)) {
            throw new SyntaxException("*102 Unknown predicate " + name);
        }
    }
}
