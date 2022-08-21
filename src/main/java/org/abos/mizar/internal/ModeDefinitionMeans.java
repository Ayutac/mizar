package org.abos.mizar.internal;

/**
 * @param specification Allowed to be null
 * @param definiens Allowed to be null
 * @param sethood Allowed to be null
 */
public record ModeDefinitionMeans(ModePattern pattern, TypeExpression specification, Definiens definiens, CorrectnessConditions correctness, Justification sethood, boolean redefinition) implements ModeDefinition {

    @Override
    public String name() {
        return pattern().name();
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        pattern.checkSyntax(environ);
        if (specification != null) {
            specification.checkSyntax(environ);
        }
        if (definiens != null) {
            definiens.checkSyntax(environ);
        }
        correctness.checkSyntax(environ);
        if (sethood != null) {
            sethood.checkSyntax(environ);
        }
    }
}
