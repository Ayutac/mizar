package org.abos.mizar.internal;

public record PredicateProperty(PredicatePropertyType type, Justification justification) implements Syntax {

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        justification.checkSyntax(environ);
    }
}
