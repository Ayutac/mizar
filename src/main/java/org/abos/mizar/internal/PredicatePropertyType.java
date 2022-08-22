package org.abos.mizar.internal;

import java.util.Locale;

public enum PredicatePropertyType {
    ASYMMETRY,
    CONNECTEDNESS,
    IRREFLEXIVITY,
    REFLEXIVITY,
    SYMMETRY;

    private final String name;

    PredicatePropertyType() {
        name = name().toLowerCase(Locale.ROOT);
    }

    /**
     * @return same as <code>name().toLowerCase(Locale.ROOT)</code>
     */
    public String getName() {
        return name;
    }
}
