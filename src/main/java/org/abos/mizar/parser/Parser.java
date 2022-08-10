package org.abos.mizar.parser;

import org.abos.mizar.Utils;
import org.abos.mizar.internal.*;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    public Article parse(final String name, final String content) throws ParseException {
        ArticleReference artName = new ArticleReference(name);
        final String noComments = Utils.removeComments(content);
        int firstBeginIndex = noComments.indexOf("begin");
        if (firstBeginIndex == -1) {
            throw new ParseException("No 'begin' in article!");
        }
        final Environ environ = parseEnviron(noComments.substring(0, firstBeginIndex).trim());
        List<TextItem> textItems = new LinkedList<>();
        // TODO parse text items
        return new Article(artName, environ, textItems);
    }

    protected Environ parseEnviron(final String environContent) throws ParseException{
        if (!environContent.startsWith("environ")) {
            throw new ParseException("Article doesn't start with 'environ'!");
        }

        var entries = new EnumMap<EnvironSymbols, Collection<ArticleReference>>(EnvironSymbols.class);
        for (final EnvironSymbols symbol : EnvironSymbols.values()) {
            entries.put(symbol, symbol.createEmptyCollection());
        }

        final String trimmedEnvironContent = environContent.substring(7).trim();
        if (trimmedEnvironContent.isEmpty()) {
            return new Environ(entries);
        }
        final String[] environParts = trimmedEnvironContent.split(";");
        for (final String part : environParts) {
            String trimmedPart = part.trim();
            int spaceIndex = trimmedPart.indexOf(' ');
            if (spaceIndex == -1) {
                throw new ParseException("\"" + part + "\" of the environ is invalid!");
            }
            boolean wasParsed = false;
            for (final EnvironSymbols symbol : EnvironSymbols.values()) {
                if (trimmedPart.startsWith(symbol.getName())) {
                    for (final String ref : trimmedPart.substring(spaceIndex+1, trimmedPart.length()).split(",")) {
                        entries.get(symbol).add(new ArticleReference(ref.trim()));
                    }
                    wasParsed = true;
                    break;
                }
            }
            if (!wasParsed) {
                throw new ParseException("Unrecognized environ part: \"" + part + "\"");
            }
        }
        return new Environ(entries);
    }

}
