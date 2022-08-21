package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record Exemplification(List<Example> examples) implements SkeletonItem {

    public Exemplification(List<Example> examples) {
        this.examples = Collections.unmodifiableList(examples);
    }

    public Exemplification(Example example) {
        this(Collections.singletonList(example));
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (Example example : examples) {
            example.checkSyntax(environ);
        }
    }
}
