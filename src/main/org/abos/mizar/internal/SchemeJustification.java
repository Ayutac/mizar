package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record SchemeJustification(String schemeRef, List<String> refs) implements SimpleJustification {

    public SchemeJustification(String schemeRef, List<String> refs) {
        this.schemeRef = schemeRef;
        this.refs = Collections.unmodifiableList(refs);
    }
}
