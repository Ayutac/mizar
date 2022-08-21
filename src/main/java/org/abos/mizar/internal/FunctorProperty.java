package org.abos.mizar.internal;

/**
 * @param justification Allowed to be null
 */
public record FunctorProperty(FunctorPropertyType type, Justification justification) implements Syntax {

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        if (justification != null) {
            justification.checkSyntax(environ);
        }
    }
}
