package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

/**
 * Is also LociDeclaration
 * @param conditions Allowed to be null
 */
public record Generalization(List<TypeListing> variables, Conditions conditions) implements DefinitionalPart, SkeletonItem {

    public Generalization(List<TypeListing> variables, Conditions conditions) {
        this.variables = Collections.unmodifiableList(variables);
        this.conditions = conditions;
    }

    public Generalization(List<TypeListing> variables) {
        this(variables, null);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (TypeListing listing : variables) {
            listing.checkSyntax(environ);
        }
        if (conditions != null) {
            conditions.checkSyntax(environ);
        }
    }
}
