package org.abos.mizar.internal;

import java.util.Collections;
import java.util.Set;

public record StraightforwardJustification(Set<String> refs) implements Justification {

    public StraightforwardJustification(Set<String> refs) {
        this.refs = Collections.unmodifiableSet(refs);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        // refs are no vocabulary
    }
}
