package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record FunctorialRegistration(TermExpression term, List<Adjective> cluster, TypeExpression type, CorrectnessConditions correctness) implements ClusterRegistration {

    public FunctorialRegistration(TermExpression term, List<Adjective> cluster, TypeExpression type, CorrectnessConditions correctness) {
        this.term = term;
        this.cluster = Collections.unmodifiableList(cluster);
        this.type = type;
        this.correctness = correctness;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        term.checkSyntax(environ);
        for (Adjective adjective : cluster) {
            adjective.checkSyntax(environ);
        }
        type.checkSyntax(environ);
        correctness.checkSyntax(environ);
    }
}
