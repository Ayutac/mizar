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
    public void testCommentsRemove() {
        Assertions.assertEquals("\r\nThis is not", Utils.removeComments(":: this is a comment\r\nThis is not"));
        Assertions.assertEquals("No comment \r\nThis is not", Utils.removeComments("No comment :: this is a comment\r\nThis is not"));
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
            Utils.loadFromMizar("/abstr/thisArticleDoesNotExist.abs");
        } catch (NoSuchFileException ex) {
            // expected
        }
    }

    @Test
    public void testArticleList() throws IOException {
        List<ArticleReference> refs = Utils.loadAllArticleNames();
        Assertions.assertFalse(refs.isEmpty());
        // HIDDEN and TARSKI_0 are, well, hidden
        Assertions.assertFalse(refs.contains(new ArticleReference("HIDDEN")));
        Assertions.assertFalse(refs.contains(new ArticleReference("TARSKI_0")));
        Assertions.assertTrue(refs.contains(new ArticleReference("TARSKI")));
        Assertions.assertTrue(refs.contains(new ArticleReference("GLIB_000")));
    }

}
