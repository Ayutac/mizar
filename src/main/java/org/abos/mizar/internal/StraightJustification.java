package org.abos.mizar.internal;

import java.util.Collections;
import java.util.Set;

public record StraightJustification(Set<String> refs) implements SimpleJustification {

    public static final StraightJustification EMPTY = new StraightJustification(Collections.emptySet());

    public StraightJustification(Set<String> refs) {
        this.refs = Collections.unmodifiableSet(refs);
    }
}
