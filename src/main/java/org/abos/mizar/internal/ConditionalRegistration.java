package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record ConditionalRegistration(List<Adjective> clusterIn, List<Adjective> clusterOut, TypeExpression expression, CorrectnessConditions correctness) implements ClusterRegistration {

    public ConditionalRegistration(List<Adjective> clusterIn, List<Adjective> clusterOut, TypeExpression expression, CorrectnessConditions correctness) {
        this.clusterIn = Collections.unmodifiableList(clusterIn);
        this.clusterOut = Collections.unmodifiableList(clusterOut);
        this.expression = expression;
        this.correctness = correctness;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (Adjective adjective : clusterIn) {
            adjective.checkSyntax(environ);
        }
        for (Adjective adjective : clusterOut) {
            adjective.checkSyntax(environ);
        }
        expression.checkSyntax(environ);
        correctness.checkSyntax(environ);
    }
}
