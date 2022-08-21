package org.abos.mizar.internal;

public record AttributeDefinition(AttributePattern pattern, Definiens definiens, CorrectnessConditions correctness, boolean redefinition) implements Definition {

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        pattern.checkSyntax(environ);
        definiens.checkSyntax(environ);
        correctness.checkSyntax(environ);
    }
}
