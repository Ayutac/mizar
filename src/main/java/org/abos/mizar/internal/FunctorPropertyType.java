package org.abos.mizar.internal;

import java.util.Locale;

public enum FunctorPropertyType {
    COMMUTATIVITY,
    IDEMPOTENCE,
    INVOLUTIVENESS,
    PROJECTIVITY;

    private final String name;

    FunctorPropertyType() {
        name = name().toLowerCase(Locale.ROOT);
    }

    /**
     * @return same as <code>name().toLowerCase(Locale.ROOT)</code>
     */
    public String getName() {
        return name;
    }
}
