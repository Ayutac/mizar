package org.abos.mizar.parser;

import org.abos.mizar.Utils;
import org.abos.mizar.internal.Article;
import org.abos.mizar.internal.ArticleReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestArticleParser {

    @Test
    public void testEmptyArticle() throws IOException, ParseException {
        String emptyArticleStr = Utils.loadFromResource("/fixtures/articles/emptyArticle.miz");
        Article emptyArticle = new ArticleParser().parse("EMPTY", emptyArticleStr);
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
        String emptyArticleStr = Utils.loadFromResource("/fixtures/articles/emptyArticleWrongOrder.miz");
        try {
            new ArticleParser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Wrong order of 'environ' and 'begin' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testEmptyArticleNoEnviron() throws IOException {
        String emptyArticleStr = Utils.loadFromResource("/fixtures/articles/emptyArticleNoEnviron.miz");
        try {
            new ArticleParser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Missing 'environ' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testEmptyArticleNoBegin() throws IOException {
        String emptyArticleStr = Utils.loadFromResource("/fixtures/articles/emptyArticleNoBegin.miz");
        try {
            new ArticleParser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Missing 'begin' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testReservationArticle() throws IOException, ParseException {
        String resArticleStr = Utils.loadFromResource("/fixtures/articles/reservationArticle.miz");
        Article resArticle = new ArticleParser().parse("RES", resArticleStr);
        Assertions.assertEquals(4, resArticle.getTextItems().size());
        // TODO content assertions
    }

    @Test
    public void testReservationArticleMissingFor() throws IOException  {
        String resArticleStr = Utils.loadFromResource("/fixtures/articles/reservationArticleMissingFor.miz");
        try {
            new ArticleParser().parse("RES", resArticleStr);
            Assertions.fail("Missing 'for' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testReservationArticleMissingAttr() throws IOException  {
        String reservationArticleStr = Utils.loadFromResource("/fixtures/articles/reservationArticleMissingAttr.miz");
        try {
            new ArticleParser().parse("RES", reservationArticleStr);
            Assertions.fail("Missing attribute after 'non' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testTheoremArticle() throws IOException, ParseException {
        String thsArticleStr = Utils.loadFromResource("/fixtures/articles/theoremArticle.miz");
        Article thsArticle = new ArticleParser().parse("THS", thsArticleStr);
        Assertions.assertEquals(3, thsArticle.getTextItems().size());
        // TODO content assertions
    }

    @Disabled
    @Test
    public void testTarski() throws IOException, ParseException {
        String tarskiStr = Utils.loadFromMizar("/mml/tarski.miz");
        Article tarski = new ArticleParser().parse("TARSKI", tarskiStr);

        Assertions.assertTrue(tarski.getEnviron().getVocabularies().contains(new ArticleReference("TARSKI")));
        Assertions.assertTrue(tarski.getEnviron().getNotations().isEmpty());
        Assertions.assertTrue(tarski.getEnviron().getConstructors().isEmpty());
        Assertions.assertTrue(tarski.getEnviron().getRegistrations().isEmpty());
        Assertions.assertTrue(tarski.getEnviron().getDefinitions().isEmpty());
        Assertions.assertTrue(tarski.getEnviron().getExpansions().isEmpty());
        Assertions.assertTrue(tarski.getEnviron().getEqualities().isEmpty());
        Assertions.assertTrue(tarski.getEnviron().getTheorems().contains(new ArticleReference("TARSKI_0")));
        Assertions.assertTrue(tarski.getEnviron().getSchemes().contains(new ArticleReference("TARSKI_0")));
        Assertions.assertTrue(tarski.getEnviron().getRequirements().isEmpty());
        // TODO test the body
    }

    @Disabled
    @Test
    public void testXboole0() throws IOException, ParseException {
        String xboole0Str = Utils.loadFromMizar("/mml/xboole_0.miz");
        Article xboole0 = new ArticleParser().parse("XBOOLE_0", xboole0Str);
        ArticleReference tarski = new ArticleReference("TARSKI");

        Assertions.assertTrue(xboole0.getEnviron().getVocabularies().contains(tarski));
        Assertions.assertTrue(xboole0.getEnviron().getVocabularies().contains(new ArticleReference("XBOOLE_0")));
        Assertions.assertTrue(xboole0.getEnviron().getNotations().contains(tarski));
        Assertions.assertTrue(xboole0.getEnviron().getConstructors().contains(tarski));
        Assertions.assertTrue(xboole0.getEnviron().getRegistrations().isEmpty());
        Assertions.assertTrue(xboole0.getEnviron().getDefinitions().contains(tarski));
        Assertions.assertTrue(xboole0.getEnviron().getExpansions().contains(tarski));
        Assertions.assertTrue(xboole0.getEnviron().getEqualities().isEmpty());
        Assertions.assertTrue(xboole0.getEnviron().getTheorems().contains(tarski));
        Assertions.assertTrue(xboole0.getEnviron().getSchemes().contains(tarski));
        Assertions.assertTrue(xboole0.getEnviron().getRequirements().isEmpty());
        // TODO test the body
    }

}
