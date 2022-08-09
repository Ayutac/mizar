package org.abos.mizar.internal;

public record CompactStatement(Proposition proposition, Justification justification) implements Statement {

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        proposition.checkSyntax(environ);
        justification.checkSyntax(environ);
    }

    @Override
    public String ref() {
        return proposition.ref();
    }
}
