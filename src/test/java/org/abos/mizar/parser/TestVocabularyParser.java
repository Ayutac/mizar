package org.abos.mizar.parser;

import org.abos.mizar.internal.ArticleReference;
import org.abos.mizar.internal.VocabularySymbols;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

public class TestVocabularyParser {

    @Test
    public void parseEmpty() throws IOException, ParseException {
        VocabularyParser parser = new VocabularyParser();
        var actualMap = parser.parse(Collections.emptySet());
        var expectedMap = new EnumMap<VocabularySymbols, Set<String>>(VocabularySymbols.class);
        for (VocabularySymbols symbol : VocabularySymbols.values()) {
            expectedMap.put(symbol, new HashSet<>());
        }
        Assertions.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void parseMissing() throws IOException, ParseException {
        VocabularyParser parser = new VocabularyParser();
        try {
            parser.parse(Collections.singleton(new ArticleReference("XBOOLE_1")));
            Assertions.fail("XBOOLE_1 has no vocabulary file!");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }

    @Test
    public void parseTarski() throws IOException, ParseException {
        VocabularyParser parser = new VocabularyParser();
        var actualMap = parser.parse(Collections.singleton(new ArticleReference("TARSKI")));
        var expectedMap = new EnumMap<VocabularySymbols, Set<String>>(VocabularySymbols.class);
        for (VocabularySymbols symbol : VocabularySymbols.values()) {
            expectedMap.put(symbol, new HashSet<>());
        }
        expectedMap.get(VocabularySymbols.R).add("c=");
        expectedMap.get(VocabularySymbols.O).add("union");
        expectedMap.get(VocabularySymbols.R).add("are_equipotent");
        Assertions.assertEquals(expectedMap, actualMap);
    }

    @Test
    public void parseTwoArticles() throws IOException, ParseException {
        VocabularyParser parser = new VocabularyParser();
        Set<ArticleReference> articleSet = new HashSet<>();
        articleSet.add(new ArticleReference("TARSKI"));
        articleSet.add(new ArticleReference("FUNCT_3"));
        var actualMap = parser.parse(articleSet);
        var expectedMap = new EnumMap<VocabularySymbols, Set<String>>(VocabularySymbols.class);
        for (VocabularySymbols symbol : VocabularySymbols.values()) {
            expectedMap.put(symbol, new HashSet<>());
        }
        // mix up the order a bit
        expectedMap.get(VocabularySymbols.R).add("c=");
        expectedMap.get(VocabularySymbols.O).add("delta");
        expectedMap.get(VocabularySymbols.O).add("union");
        expectedMap.get(VocabularySymbols.O).add("chi");
        expectedMap.get(VocabularySymbols.R).add("are_equipotent");
        expectedMap.get(VocabularySymbols.O).add("incl");
        Assertions.assertEquals(expectedMap, actualMap);
    }

}
