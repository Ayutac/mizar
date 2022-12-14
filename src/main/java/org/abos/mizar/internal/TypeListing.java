package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

/**
 * Is also Qualified-Segment
 */
public record TypeListing(TypeExpression type, List<String> variables) implements DefinitionalPart, RegistrationPart, NotationPart {

    public TypeListing(TypeExpression type, List<String> variables) {
        this.type = type;
        this.variables = Collections.unmodifiableList(variables);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        type.checkSyntax(environ);
    }

}
