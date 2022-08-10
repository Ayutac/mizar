package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record TypeExpression(RadixType radixType, List<Adjective> adjectives) implements Syntax {

    public TypeExpression(RadixType radixType, List<Adjective> adjectives) {
        this.radixType = radixType;
        this.adjectives = Collections.unmodifiableList(adjectives);
    }

    public TypeExpression(RadixType radixType) {
        this(radixType, Collections.emptyList());
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        radixType.checkSyntax(environ);
        for (Adjective adj : adjectives) {
            adj.checkSyntax(environ);
        }
    }
}
