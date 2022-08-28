package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record ExistentialRegistration(List<Adjective> cluster, TypeExpression expression, CorrectnessConditions correctness) implements ClusterRegistration {

    public ExistentialRegistration(List<Adjective> cluster, TypeExpression expression, CorrectnessConditions correctness) {
        this.cluster = Collections.unmodifiableList(cluster);
        this.expression = expression;
        this.correctness = correctness;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (Adjective adjective : cluster) {
            adjective.checkSyntax(environ);
        }
        expression.checkSyntax(environ);
        correctness.checkSyntax(environ);
    }
}
