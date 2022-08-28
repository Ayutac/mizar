package org.abos.mizar.parser;

import org.abos.mizar.Utils;
import org.abos.mizar.internal.Article;
import org.abos.mizar.internal.ArticleReference;
import org.abos.mizar.internal.DefinitionalItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class TestArticleParser {

    @Test
    public void testEmptyArticle() throws Exception {
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
    public void testEmptyArticleWrong() throws Exception {
        String emptyArticleStr = Utils.loadFromResource("/fixtures/articles/emptyArticleWrongOrder.miz");
        try {
            new ArticleParser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Wrong order of 'environ' and 'begin' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testEmptyArticleNoEnviron() throws Exception {
        String emptyArticleStr = Utils.loadFromResource("/fixtures/articles/emptyArticleNoEnviron.miz");
        try {
            new ArticleParser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Missing 'environ' must cause a wrapped ParserException!");
        } catch (ExecutionException ex) {
            // expected
            Assertions.assertInstanceOf(ParseException.class, ex.getCause());
        }
    }

    @Test
    public void testEmptyArticleNoBegin() throws Exception {
        String emptyArticleStr = Utils.loadFromResource("/fixtures/articles/emptyArticleNoBegin.miz");
        try {
            new ArticleParser().parse("EMPTY", emptyArticleStr);
            Assertions.fail("Missing 'begin' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testReservationArticle() throws Exception {
        String resArticleStr = Utils.loadFromResource("/fixtures/articles/reservationArticle.miz");
        Article resArticle = new ArticleParser().parse("RES", resArticleStr);
        Assertions.assertEquals(4, resArticle.getTextItems().size());
        // TODO content assertions
    }

    @Test
    public void testReservationArticleMissingFor() throws Exception  {
        String resArticleStr = Utils.loadFromResource("/fixtures/articles/reservationArticleMissingFor.miz");
        try {
            new ArticleParser().parse("RES", resArticleStr);
            Assertions.fail("Missing 'for' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testReservationArticleMissingAttr() throws Exception  {
        String reservationArticleStr = Utils.loadFromResource("/fixtures/articles/reservationArticleMissingAttr.miz");
        try {
            new ArticleParser().parse("RES", reservationArticleStr);
            Assertions.fail("Missing attribute after 'non' must cause a ParserException!");
        } catch (ParseException ex) {
            // expected
        }
    }

    @Test
    public void testDefinitionAttrArticle() throws Exception {
        String attrArticleStr = Utils.loadFromResource("/fixtures/articles/definitionAttrArticle.miz");
        Article attrArticle = new ArticleParser().parse("ATTR", attrArticleStr);
        Assertions.assertEquals(2, attrArticle.getTextItems().size());
        DefinitionalItem definitional = (DefinitionalItem)attrArticle.getTextItems().get(0);
        Assertions.assertEquals(3, definitional.parts().size());
        definitional = (DefinitionalItem)attrArticle.getTextItems().get(1);
        Assertions.assertEquals(2, definitional.parts().size());
        // TODO more content assertions
    }

    @Test
    public void testDefinitionPredArticle() throws Exception {
        String predArticleStr = Utils.loadFromResource("/fixtures/articles/definitionPredArticle.miz");
        Article predArticle = new ArticleParser().parse("PRED", predArticleStr);
        Assertions.assertEquals(2, predArticle.getTextItems().size());
        DefinitionalItem definitional = (DefinitionalItem)predArticle.getTextItems().get(0);
        Assertions.assertEquals(3, definitional.parts().size());
        definitional = (DefinitionalItem)predArticle.getTextItems().get(1);
        Assertions.assertEquals(3, definitional.parts().size());
        // TODO more content assertions
    }

    @Test
    public void testDefinitionStructArticle() throws Exception {
        String structArticleStr = Utils.loadFromResource("/fixtures/articles/definitionStructArticle.miz");
        Article structArticle = new ArticleParser().parse("STRUCT", structArticleStr);
        Assertions.assertEquals(3, structArticle.getTextItems().size());
        DefinitionalItem definitional = (DefinitionalItem)structArticle.getTextItems().get(0);
        Assertions.assertEquals(1, definitional.parts().size());
        definitional = (DefinitionalItem)structArticle.getTextItems().get(1);
        Assertions.assertEquals(2, definitional.parts().size());
        definitional = (DefinitionalItem)structArticle.getTextItems().get(2);
        Assertions.assertEquals(1, definitional.parts().size());
        // TODO more content assertions
    }

    @Test
    public void testTheoremArticle() throws Exception {
        String thsArticleStr = Utils.loadFromResource("/fixtures/articles/theoremArticle.miz");
        Article thsArticle = new ArticleParser().parse("THS", thsArticleStr);
        Assertions.assertEquals(3, thsArticle.getTextItems().size());
        // TODO content assertions
    }

    @Disabled
    @Test
    public void testTarski() throws Exception {
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
    public void testXboole0() throws Exception {
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
