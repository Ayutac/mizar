package org.abos.mizar.parser;

import org.abos.mizar.internal.Article;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestParser {

    @Test
    public void testEmptyArticle() throws IOException {
        String emptyArticleStr = Parser.loadFromFile("/fixtures/articles/emptyArticle.miz");
        Article emptyArticle = new Parser().parse("EMPTY", emptyArticleStr);
        Assertions.assertTrue(emptyArticle.getEnviron().getVocabularies().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getNotations().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getConstructors().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getRegistrations().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getDefinitions().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getExpansions().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getEqualities().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getTheorems().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getSchemes().isEmpty());
        Assertions.assertTrue(emptyArticle.getEnviron().getRequirements().isEmpty());
        Assertions.assertTrue(emptyArticle.getTextItems().isEmpty());
    }

}
