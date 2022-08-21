package org.abos.mizar.internal;

/**
 *
 * @param variable Allowed to be null
 */
public record Example(TermExpression expression, String variable) implements Syntax {

    public Example(TermExpression expression) {
        this(expression, null);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        expression.checkSyntax(environ);
    }
}
