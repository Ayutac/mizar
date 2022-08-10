package org.abos.mizar.internal;

/**
 * Is also Single-Assumption
 */
public record Proposition(FormulaExpression expr, String ref) implements Assumption {

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        expr.checkSyntax(environ);
    }
}
