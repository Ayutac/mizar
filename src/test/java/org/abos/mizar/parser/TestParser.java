package org.abos.mizar.parser;

import org.abos.mizar.Utils;
import org.abos.mizar.internal.Article;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestParser {

    @Test
    public void testEmptyArticle() throws IOException, ParseException {
        String emptyArticleStr = Utils.loadFromFile("/fixtures/articles/emptyArticle.miz");
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

    @Test
    public void testEmptyArticleWrong() throws IOException {
        String emptyArticleStr = Utils.loadFromFile("/fixtures/articles/emptyArticleWrongOrder.miz");
        try {
            new Parser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Wrong order of 'environ' and 'begin' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testEmptyArticleNoEnviron() throws IOException {
        String emptyArticleStr = Utils.loadFromFile("/fixtures/articles/emptyArticleNoEnviron.miz");
        try {
            new Parser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Missing 'environ' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testEmptyArticleNoBegin() throws IOException {
        String emptyArticleStr = Utils.loadFromFile("/fixtures/articles/emptyArticleNoBegin.miz");
        try {
            new Parser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Missing 'begin' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

}
