package org.abos.mizar.internal;

public record CompactStatement(Proposition proposition, Justification justification, boolean theorem) implements Statement {

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        proposition.checkSyntax(environ);
        justification.checkSyntax(environ);
    }

    public String ref() {
        return proposition.ref();
    }
}
