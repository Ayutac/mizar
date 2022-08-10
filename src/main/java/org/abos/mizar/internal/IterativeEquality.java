package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record IterativeEquality(List<TermExpression> terms, List<SimpleJustification> justifications, String ref) implements Statement {

    public IterativeEquality(List<TermExpression> terms, List<SimpleJustification> justifications, String ref) {
        if (terms.size() != justifications.size() + 1) {
            throw new IllegalArgumentException("Each term after the first one needs a justification (might be empty)!");
        }
        this.terms = Collections.unmodifiableList(terms);
        this.justifications = Collections.unmodifiableList(justifications);
        this.ref = ref;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (TermExpression term : terms) {
            term.checkSyntax(environ);
        }
    }
}
