package org.abos.mizar.internal;

import java.io.IOException;
import java.util.*;

public final class Environ {

    private final Map<EnvironSymbols, Collection<ArticleReference>> entries = new EnumMap<>(EnvironSymbols.class);

    private boolean loaded = false;

    private final Map<VocabularySymbols, Set<String>> vocabulary = new EnumMap<>(VocabularySymbols.class);

    public Environ(Set<ArticleReference> vocabularies, List<ArticleReference> notations, Set<ArticleReference> constructors, Set<ArticleReference> registrations, Set<ArticleReference> definitions, Set<ArticleReference> expansions, Set<ArticleReference> equalities, Set<ArticleReference> theorems, Set<ArticleReference> schemes, Set<ArticleReference> requirements) {
        entries.put(EnvironSymbols.VOCABULARIES, Collections.unmodifiableSet(vocabularies));
        entries.put(EnvironSymbols.NOTATIONS, Collections.unmodifiableList(notations));
        entries.put(EnvironSymbols.CONSTRUCTORS, Collections.unmodifiableSet(constructors));
        entries.put(EnvironSymbols.REGISTRATIONS, Collections.unmodifiableSet(registrations));
        entries.put(EnvironSymbols.DEFINITIONS, Collections.unmodifiableSet(definitions));
        entries.put(EnvironSymbols.EXPANSIONS, Collections.unmodifiableSet(expansions));
        entries.put(EnvironSymbols.EQUALITIES, Collections.unmodifiableSet(equalities));
        entries.put(EnvironSymbols.THEOREMS, Collections.unmodifiableSet(theorems));
        entries.put(EnvironSymbols.SCHEMES, Collections.unmodifiableSet(schemes));
        entries.put(EnvironSymbols.REQUIREMENTS, Collections.unmodifiableSet(requirements));
        for (VocabularySymbols symbol : VocabularySymbols.values()) {
            vocabulary.put(symbol, new HashSet<>());
        }
    }

    public Environ(Map<EnvironSymbols, Collection<ArticleReference>> entries) {
        this((Set<ArticleReference>)entries.get(EnvironSymbols.VOCABULARIES),
                (List<ArticleReference>)entries.get(EnvironSymbols.NOTATIONS),
                (Set<ArticleReference>)entries.get(EnvironSymbols.CONSTRUCTORS),
                (Set<ArticleReference>)entries.get(EnvironSymbols.REGISTRATIONS),
                (Set<ArticleReference>)entries.get(EnvironSymbols.DEFINITIONS),
                (Set<ArticleReference>)entries.get(EnvironSymbols.EXPANSIONS),
                (Set<ArticleReference>)entries.get(EnvironSymbols.EQUALITIES),
                (Set<ArticleReference>)entries.get(EnvironSymbols.THEOREMS),
                (Set<ArticleReference>)entries.get(EnvironSymbols.SCHEMES),
                (Set<ArticleReference>)entries.get(EnvironSymbols.REQUIREMENTS));
    }

    public Set<ArticleReference> getVocabularies() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.VOCABULARIES);
    }

    public List<ArticleReference> getNotations() {
        return (List<ArticleReference>)entries.get(EnvironSymbols.NOTATIONS);
    }

    public Set<ArticleReference> getConstructors() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.CONSTRUCTORS);
    }

    public Set<ArticleReference> getRegistrations() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.REGISTRATIONS);
    }

    public Set<ArticleReference> getDefinitions() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.DEFINITIONS);
    }

    public Set<ArticleReference> getExpansions() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.EXPANSIONS);
    }

    public Set<ArticleReference> getEqualities() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.EQUALITIES);
    }

    public Set<ArticleReference> getTheorems() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.THEOREMS);
    }

    public Set<ArticleReference> getSchemes() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.SCHEMES);
    }

    public Set<ArticleReference> getRequirements() {
        return (Set<ArticleReference>)entries.get(EnvironSymbols.REQUIREMENTS);
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