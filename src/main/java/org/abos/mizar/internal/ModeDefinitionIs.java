package org.abos.mizar.internal;

/**
 *
 * @param sethood Allowed to be null
 */
public record ModeDefinitionIs(ModePattern pattern, TypeExpression expression, Justification sethood) implements ModeDefinition {

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        pattern.checkSyntax(environ);
        expression.checkSyntax(environ);
        if (sethood != null) {
            sethood.checkSyntax(environ);
        }
    }

    @Override
    public String name() {
        return pattern().name();
    }

    @Override
    public boolean redefinition() {
        return false;
    }
}
