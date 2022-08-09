package org.abos.mizar.internal;

import java.io.IOException;
import java.util.*;

public final class Environ {

    private final Set<ArticleReference> vocabularies;

    private final List<ArticleReference> notations;

    private final Set<ArticleReference> constructors;

    private final Set<ArticleReference> registrations;

    private final Set<ArticleReference> definitions;

    private final Set<ArticleReference> expansions;

    private final Set<ArticleReference> equalities;

    private final Set<ArticleReference> theorems;

    private final Set<ArticleReference> schemes;

    private final Set<ArticleReference> requirements;

    private boolean loaded = false;

    private final Map<VocabularySymbols, Set<String>> vocabulary = new EnumMap<>(VocabularySymbols.class);

    public Environ(Set<ArticleReference> vocabularies, List<ArticleReference> notations, Set<ArticleReference> constructors, Set<ArticleReference> registrations, Set<ArticleReference> definitions, Set<ArticleReference> expansions, Set<ArticleReference> equalities, Set<ArticleReference> theorems, Set<ArticleReference> schemes, Set<ArticleReference> requirements) {
        this.vocabularies = Collections.unmodifiableSet(vocabularies);
        this.notations = Collections.unmodifiableList(notations);
        this.constructors = Collections.unmodifiableSet(constructors);
        this.registrations = Collections.unmodifiableSet(registrations);
        this.definitions = Collections.unmodifiableSet(definitions);
        this.expansions = Collections.unmodifiableSet(expansions);
        this.equalities = Collections.unmodifiableSet(equalities);
        this.theorems = Collections.unmodifiableSet(theorems);
        this.schemes = Collections.unmodifiableSet(schemes);
        this.requirements = Collections.unmodifiableSet(requirements);
        for (VocabularySymbols symbol : VocabularySymbols.values()) {
            vocabulary.put(symbol, new HashSet<>());
        }
    }

    public Set<ArticleReference> getVocabularies() {
        return vocabularies;
    }

    public List<ArticleReference> getNotations() {
        return notations;
    }

    public Set<ArticleReference> getConstructors() {
        return constructors;
    }

    public Set<ArticleReference> getRegistrations() {
        return registrations;
    }

    public Set<ArticleReference> getDefinitions() {
        return definitions;
    }

    public Set<ArticleReference> getExpansions() {
        return expansions;
    }

    public Set<ArticleReference> getEqualities() {
        return equalities;
    }

    public Set<ArticleReference> getTheorems() {
        return theorems;
    }

    public Set<ArticleReference> getSchemes() {
        return schemes;
    }

    public Set<ArticleReference> getRequirements() {
        return requirements;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void load() throws IOException {
        if (isLoaded()) {
            throw new IllegalStateException("Environ already loaded!");
        }
        // load in-built expressions
        vocabulary.get(VocabularySymbols.M).add("set");
        vocabulary.get(VocabularySymbols.R).add("=");
        // TODO load the rest in
        loaded = true;
    }

    public boolean isValidSymbol(String name, VocabularySymbols type) {
        return vocabulary.get(type).contains(name);
    }

}