package org.abos.mizar.internal;

import java.util.Locale;

public enum CorrectnessConditionType {
    COHERENCE,
    COMPATIBILITY,
    CONSISTENCY,
    EXISTENCE,
    REDUCIBILITY,
    UNIQUENESS;

    private final String name;

    CorrectnessConditionType() {
        name = name().toLowerCase(Locale.ROOT);
    }

    /**
     * @return same as <code>name().toLowerCase(Locale.ROOT)</code>
     */
    public String getName() {
        return name;
    }
}
