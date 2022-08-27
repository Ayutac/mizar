package org.abos.mizar.parser;

import org.abos.mizar.Utils;
import org.abos.mizar.internal.ArticleReference;
import org.abos.mizar.internal.VocabularySymbols;

import java.io.IOException;
import java.util.*;

public class VocabularyParser {

    public Map<VocabularySymbols, Set<String>> parse(Set<ArticleReference> refs) throws IOException, ParseException, IllegalStateException {
        final var queue = new LinkedList<>(refs);
        queue.sort(Utils.getLexicalArticleComparator());
        final var map = new EnumMap<VocabularySymbols, Set<String>>(VocabularySymbols.class);
        for (VocabularySymbols symbol : VocabularySymbols.values()) {
            map.put(symbol, new HashSet<>());
        }
        final Iterator<String> vocIt = Utils.loadVocabularyFile().iterator();

        String line = null;
        boolean scanArticle = false;
        String article = null;
        String vocStr = null;
        VocabularySymbols vocType = null;
        int spaceIndex = -1;

        while (vocIt.hasNext()) {
            line = vocIt.next();
            // new article vocabulary
            if (line.startsWith("#")) {
                article = line.substring(1);
                if (queue.isEmpty()) {
                    break;
                }
                if (article.equals(queue.getFirst().getRefS())) {
                    scanArticle = true;
                    queue.pop();
                }
                else {
                    scanArticle = false;
                }
                if (vocIt.hasNext()) {
                    // line after article name is irrelevant
                    vocIt.next();
                }
            }
            // same article vocabulary, parse vocabulary entry
            else if (scanArticle) {
                try {
                    vocType = VocabularySymbols.valueOf(line.substring(0,1));
                }
                catch (IllegalArgumentException ex) {
                    throw new ParseException("Unknown vocabulary type!", ex);
                }
                vocStr = line.substring(1);
                spaceIndex = vocStr.indexOf(' ');
                if (spaceIndex != -1) {
                    vocStr = vocStr.substring(0, spaceIndex);
                }
                map.get(vocType).add(vocStr);
            }
        }

        if (!queue.isEmpty()) {
            throw new IllegalStateException("No vocabulary entry found for "+queue.getFirst());
        }

        return map;
    }
}
