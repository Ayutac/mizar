package org.abos.mizar.core;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TypeListing implements Syntax {

    private final TypeExpression type;

    private final List<String> variables;

    public TypeListing(TypeExpression type, List<String> variables) {
        this.type = type;
        this.variables = Collections.unmodifiableList(variables);
    }

    public TypeExpression getType() {
        return type;
    }

    public List<String> getVariables() {
        return variables;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        type.checkSyntax(environ);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeListing that = (TypeListing) o;
        return Objects.equals(type, that.type) && Objects.equals(variables, that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, variables);
    }
}
