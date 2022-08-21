package org.abos.mizar.internal;

public record AttributePattern(String name, String variable) implements Syntax {

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        if (!environ.isValidSymbol(name, VocabularySymbols.V)) {
            throw new SyntaxException("*106 Unknown attribute " + name);
        }
    }
}
