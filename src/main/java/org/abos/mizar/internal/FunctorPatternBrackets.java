package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record FunctorPatternBrackets(String leftBracket, List<String> variables, String rightBracket) implements FunctorPattern {

    public FunctorPatternBrackets(String leftBracket, List<String> variables, String rightBracket) {
        this.leftBracket = leftBracket;
        this.variables = Collections.unmodifiableList(variables);
        this.rightBracket = rightBracket;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        if (!environ.isValidSymbol(leftBracket, VocabularySymbols.K)) {
            throw new SyntaxException("Unknown left bracket " + leftBracket);
        }
        if (!environ.isValidSymbol(rightBracket, VocabularySymbols.L)) {
            throw new SyntaxException("Unknown right bracket " + rightBracket);
        }
    }
}
