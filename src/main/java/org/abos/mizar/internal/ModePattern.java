package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record ModePattern(String name, List<String> variables) implements Syntax {

    public ModePattern(String name, List<String> variables) {
        this.name = name;
        this.variables = Collections.unmodifiableList(variables);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        if (!environ.isValidSymbol(name, VocabularySymbols.M)) {
            throw new SyntaxException("*101 Unknown mode " + name);
        }
    }
}
