package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public class Article {

    private final ArticleReference name;

    private final Environ environ;

    private final List<TextItem> textItems;

    public Article(ArticleReference name, Environ environ, List<TextItem> textItems) {
        this.name = name;
        this.environ = environ;
        this.textItems = Collections.unmodifiableList(textItems);
    }

    public ArticleReference getName() {
        return name;
    }

    public Environ getEnviron() {
        return environ;
    }

    public List<TextItem> getTextItems() {
        return textItems;
    }
}
