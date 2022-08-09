package org.abos.mizar.internal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

public class TestEnviron {

    public static Environ createEmptyEnviron() {
        return new Environ(Collections.emptySet(), Collections.emptyList(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    @Test
    public void testEmptyEnviron() throws IOException {
        Environ environ = createEmptyEnviron();
        environ.load();
        Assertions.assertTrue(environ.getVocabularies().isEmpty());
        Assertions.assertTrue(environ.isValidSymbol("set", VocabularySymbols.M));
        Assertions.assertTrue(environ.isValidSymbol("=", VocabularySymbols.R));
        Assertions.assertFalse(environ.isValidSymbol("object", VocabularySymbols.M));
    }

}
