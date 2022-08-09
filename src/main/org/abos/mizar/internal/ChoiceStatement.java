package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

/**
 * Is also Existential-Assumption
 */
public record ChoiceStatement(List<TypeListing> variables, Conditions conditions) implements Statement, Assumption {

    public ChoiceStatement(List<TypeListing> variables, Conditions conditions) {
        if (variables.isEmpty()) {
            throw new IllegalArgumentException("At least one variable must be declared!");
        }
        this.variables = Collections.unmodifiableList(variables);
        this.conditions = conditions;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (TypeListing listing : variables) {
            listing.checkSyntax(environ);
        }
        conditions.checkSyntax(environ);
    }
}
