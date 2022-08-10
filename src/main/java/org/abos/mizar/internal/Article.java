package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public class Article {

    private final String name;

    private final Environ environ;

    private final List<TextItem> textItems;

    public Article(String name, Environ environ, List<TextItem> textItems) {
        this.name = name;
        this.environ = environ;
        this.textItems = Collections.unmodifiableList(textItems);
    }

    public String getName() {
        return name;
    }

    public Environ getEnviron() {
        return environ;
    }

    public List<TextItem> getTextItems() {
        return textItems;
    }
}
