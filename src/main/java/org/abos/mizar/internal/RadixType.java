package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record RadixType(String name, boolean mode, List<TermExpression> termList) implements Syntax {

    public RadixType(String name, boolean mode, List<TermExpression> termList) {
        this.name = name;
        this.mode = mode;
        this.termList = Collections.unmodifiableList(termList);
    }

    public RadixType(String name, boolean mode) {
        this(name, mode, Collections.emptyList());
    }

    public boolean structure() {
        return !mode();
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        if (mode && !environ.isValidSymbol(name, VocabularySymbols.M)) {
            throw new SyntaxException("*101 Unknown mode " + name);
        }
        else if (!mode && !environ.isValidSymbol(name, VocabularySymbols.G)) {
            throw new SyntaxException("*104 Unknown structure " + name);
        }
        for (TermExpression term : termList) {
            term.checkSyntax(environ);
        }
    }

}
