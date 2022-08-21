package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record Ancestors(List<StructureTypeExpression> ancestors) implements Syntax {

    public Ancestors(List<StructureTypeExpression> ancestors) {
        this.ancestors = Collections.unmodifiableList(ancestors);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (StructureTypeExpression ancestor : ancestors) {
            ancestor.checkSyntax(environ);
        }
    }
}
