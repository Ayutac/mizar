package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record Reasoning(List<ReasoningItem> items) implements Statement, Justification {

    public Reasoning(List<ReasoningItem> items) {
        this.items = Collections.unmodifiableList(items);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (ReasoningItem item : items) {
            item.checkSyntax(environ);
        }
    }
}
