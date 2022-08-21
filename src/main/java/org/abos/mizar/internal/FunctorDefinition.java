package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

/**
 * @param specification Allowed to be null
 * @param definiens Allowed to be null
 */
public record FunctorDefinition(FunctorPattern pattern, TypeExpression specification, boolean equals, Definiens definiens, CorrectnessConditions correctness, List<FunctorProperty> properties, boolean redefinition) implements Definition {

    public FunctorDefinition(FunctorPattern pattern, TypeExpression specification, boolean equals, Definiens definiens, CorrectnessConditions correctness, List<FunctorProperty> properties, boolean redefinition) {
        this.pattern = pattern;
        this.specification = specification;
        this.equals = equals;
        this.definiens = definiens;
        this.correctness = correctness;
        this.properties = Collections.unmodifiableList(properties);
        this.redefinition = redefinition;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        pattern.checkSyntax(environ);
        if (specification != null) {
            specification.checkSyntax(environ);
        }
        if (definiens != null) {
            definiens.checkSyntax(environ);
        }
        correctness.checkSyntax(environ);
        for (FunctorProperty property : properties) {
            property.checkSyntax(environ);
        }
    }
}
