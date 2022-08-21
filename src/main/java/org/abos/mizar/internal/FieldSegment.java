package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record FieldSegment(List<String> selectors, TypeExpression specification) implements Syntax {

    public FieldSegment(List<String> selectors, TypeExpression specification) {
        this.selectors = Collections.unmodifiableList(selectors);
        this.specification = specification;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (String selector : selectors) {
            if (!environ.isValidSymbol(selector, VocabularySymbols.U)) {
                throw new SyntaxException("*126 Unknown selector functor " + selector);
            }
        }
        specification.checkSyntax(environ);
    }
}
