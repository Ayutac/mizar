package org.abos.mizar;

import org.abos.mizar.internal.ArticleReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class TestUtils {

    @Test
    public void testMizfiles() {
        Assertions.assertNotNull(Utils.MIZFILES);
    }

    @Test
    public void testUnknownFixture() throws IOException {
        try {
            Utils.loadFromResource("/fixtures/thisFileDoesNotExist.txt");
        } catch (NoSuchFileException ex) {
            // expected
        }
    }

    @Test
    public void testUnknownArticle() throws IOException {
        try {
            Utils.loadFromMizar("/abs/thisArticleDoesNotExist.abs");
        } catch (NoSuchFileException ex) {
            // expected
        }
    }

    @Test
    public void testArticleList() throws IOException {
        List<ArticleReference> refs = Utils.loadAllArticleNames();
        Assertions.assertFalse(refs.isEmpty());
        // HIDDEN is, well, hidden
        Assertions.assertFalse(refs.contains(new ArticleReference("HIDDEN")));
        Assertions.assertTrue(refs.contains(new ArticleReference("TARSKI")));
        Assertions.assertTrue(refs.contains(new ArticleReference("GLIB_000")));
    }

}
