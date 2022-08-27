package org.abos.mizar.internal;

import org.abos.mizar.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class TestEnviron {

    public static Environ createEmptyEnviron() {
        return new Environ(Collections.emptySet(), Collections.emptyList(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    public static Environ createVocEnviron(Set<ArticleReference> vocabularies) {
        return new Environ(vocabularies, Collections.emptyList(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
    }

    @Test
    public void testEmptyEnviron() throws IOException, ParseException {
        Environ environ = createEmptyEnviron();
        environ.load();
        Assertions.assertTrue(environ.getVocabularies().isEmpty());
        Assertions.assertTrue(environ.isValidSymbol("set", VocabularySymbols.M));
        Assertions.assertTrue(environ.isValidSymbol("=", VocabularySymbols.R));
        Assertions.assertFalse(environ.isValidSymbol("object", VocabularySymbols.M));
    }

    @Test
    public void testTarskiVocEnviron() throws IOException, ParseException {
        Environ environ = createVocEnviron(Collections.singleton(new ArticleReference("TARSKI")));
        environ.load();
        Assertions.assertFalse(environ.getVocabularies().isEmpty());
        Assertions.assertTrue(environ.isValidSymbol("set", VocabularySymbols.M));
        Assertions.assertTrue(environ.isValidSymbol("=", VocabularySymbols.R));
        Assertions.assertFalse(environ.isValidSymbol("object", VocabularySymbols.M));
        Assertions.assertTrue(environ.isValidSymbol("union", VocabularySymbols.O));
        Assertions.assertTrue(environ.isValidSymbol("c=", VocabularySymbols.R));
        Assertions.assertTrue(environ.isValidSymbol("are_equipotent", VocabularySymbols.R));
    }

}
