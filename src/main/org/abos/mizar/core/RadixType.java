package org.abos.mizar.core;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RadixType implements Syntax {

    private final String name;

    private final boolean mode;

    private final List<TermExpression> termList;

    public RadixType(String name, boolean mode, List<TermExpression> termList) {
        this.name = name;
        this.mode = mode;
        this.termList = Collections.unmodifiableList(termList);
    }

    public RadixType(String name, boolean mode) {
        this(name, mode, Collections.emptyList());
    }

    public String getName() {
        return name;
    }

    public boolean isMode() {
        return mode;
    }

    public boolean isStructure() {
        return !isMode();
    }

    public List<TermExpression> getTermList() {
        return termList;
    }

    @Override
    public void checkSyntax(Environ environ) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RadixType radixType = (RadixType) o;
        return mode == radixType.mode && Objects.equals(name, radixType.name) && Objects.equals(termList, radixType.termList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mode, termList);
    }
}
