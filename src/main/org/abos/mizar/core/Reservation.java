package org.abos.mizar.core;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Reservation implements TextItem {

    private final Map<TypeExpression, List<String>> variables;

    public Reservation(Map<TypeExpression, List<String>> variables) {
        this.variables = variables;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (TypeExpression type : variables.keySet()) {
            type.checkSyntax(environ);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(variables, that.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables);
    }
}
