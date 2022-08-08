package org.abos.mizar.core;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TypeExpression implements Syntax {

    private final RadixType radixType;

    private final List<Adjective> adjectives;

    public TypeExpression(RadixType radixType, List<Adjective> adjectives) {
        this.radixType = radixType;
        this.adjectives = Collections.unmodifiableList(adjectives);
    }

    public TypeExpression(RadixType radixType) {
        this(radixType, Collections.emptyList());
    }

    public RadixType getRadixType() {
        return radixType;
    }

    public List<Adjective> getAdjectives() {
        return adjectives;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        radixType.checkSyntax(environ);
        for (Adjective adj : adjectives) {
            adj.checkSyntax(environ);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeExpression that = (TypeExpression) o;
        return Objects.equals(radixType, that.radixType) && Objects.equals(adjectives, that.adjectives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(radixType, adjectives);
    }
}
