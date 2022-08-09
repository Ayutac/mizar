package org.abos.mizar.internal;

public enum VocabularySymbols {

    R("Predicate"),

    O("Functor"),

    M("Mode"),

    G("Structure"),

    U("Selector"),

    V("Attribute"),

    K("Left Functor Bracket"),

    L("Right Functor Bracket");

    private final String name;

    VocabularySymbols(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}