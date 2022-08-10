package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record Conditions(List<Proposition> propositions) implements Assumption {

    public Conditions(List<Proposition> propositions) {
        if (propositions.isEmpty()) {
            throw new IllegalArgumentException("Conditions must contain at least one proposition!");
        }
        this.propositions = Collections.unmodifiableList(propositions);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (Proposition prop :propositions) {
            prop.checkSyntax(environ);
        }
    }
}
