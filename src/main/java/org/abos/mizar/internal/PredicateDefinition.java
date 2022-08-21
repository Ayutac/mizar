package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record PredicateDefinition(PredicatePattern pattern, Definiens definiens, CorrectnessConditions correctness, List<PredicateProperty> properties, boolean redefinition) implements Definition {

    public PredicateDefinition(PredicatePattern pattern, Definiens definiens, CorrectnessConditions correctness, List<PredicateProperty> properties, boolean redefinition) {
        this.pattern = pattern;
        this.definiens = definiens;
        this.correctness = correctness;
        this.properties = Collections.unmodifiableList(properties);
        this.redefinition = redefinition;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        pattern.checkSyntax(environ);
        definiens.checkSyntax(environ);
        correctness.checkSyntax(environ);
        for (PredicateProperty property : properties) {
            property.checkSyntax(environ);
        }
    }
}
