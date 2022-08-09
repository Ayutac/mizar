package org.abos.mizar.internal;

import java.util.*;

public enum EnvironSymbols {

    VOCABULARIES,

    NOTATIONS,

    CONSTRUCTORS,

    REGISTRATIONS,

    DEFINITIONS,

    EXPANSIONS,

    EQUALITIES,

    THEOREMS,

    SCHEMES,

    REQUIREMENTS;

    private final String name;

    EnvironSymbols() {
        this.name = name().toLowerCase(Locale.ROOT);
    }

    public String getName() {
        return name;
    }

    public Collection<ArticleReference> createEmptyCollection() {
        if (this == NOTATIONS) {
            return new LinkedList<>();
        }
        return new HashSet<>();
    }
}