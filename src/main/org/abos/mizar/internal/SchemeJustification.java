package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record SchemeJustification(String schemeRef, List<String> refs) implements Justification {

    public SchemeJustification(String schemeRef, List<String> refs) {
        this.schemeRef = schemeRef;
        this.refs = Collections.unmodifiableList(refs);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        // refs are no vocabulary
    }
}
