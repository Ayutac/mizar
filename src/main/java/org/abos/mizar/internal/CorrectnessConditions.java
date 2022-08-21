package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

/**
 * @param correctness Allowed to be null
 */
public record CorrectnessConditions(List<CorrectnessCondition> conditions, Justification correctness) implements Syntax {

    public CorrectnessConditions(List<CorrectnessCondition> conditions, Justification correctness) {
        this.conditions = Collections.unmodifiableList(conditions);
        this.correctness = correctness;
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (CorrectnessCondition condition : conditions) {
            condition.checkSyntax(environ);
        }
        if (correctness != null) {
            correctness.checkSyntax(environ);
        }
    }
}
