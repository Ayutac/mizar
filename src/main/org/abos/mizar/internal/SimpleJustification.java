package org.abos.mizar.internal;

public interface SimpleJustification extends Justification {

    @Override
    default void checkSyntax(Environ environ) throws SyntaxException {
        // refs are no vocabulary
    }
}
