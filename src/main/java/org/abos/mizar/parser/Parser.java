package org.abos.mizar.parser;

import org.abos.mizar.Utils;
import org.abos.mizar.internal.*;

import java.util.*;

public class Parser {

    public Article parse(final String name, final String content) throws ParseException {
        final ArticleReference artName = new ArticleReference(name);
        String remainder = Utils.removeComments(content);
        int firstBeginIndex = remainder.indexOf("begin");
        if (firstBeginIndex == -1) {
            throw new ParseException("No 'begin' in article!");
        }
        final Environ environ = parseEnviron(remainder.substring(0, firstBeginIndex).trim());
        List<TextItem> textItems = new LinkedList<>();
        remainder = remainder.substring(firstBeginIndex+5).trim();
        while (!remainder.isEmpty()) {
            if (remainder.startsWith(TextItem.RESERVATION)) {
                remainder = parseReservation(remainder.substring(TextItem.RESERVATION.length()).trim(), textItems);
            }
            // TODO parse remaining text items
        }
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
                    for (final String ref : trimmedPart.substring(spaceIndex+1).split(",")) {
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

    protected String parseReservation(final String remainder, final List<TextItem> textItems) throws ParseException {
        int endIndex = remainder.indexOf(";");
        if (endIndex == -1) {
            throw new ParseException("No end of '" + TextItem.RESERVATION + "'!");
        }
        textItems.add(new Reservation(parseTypeListings(remainder.substring(0, endIndex).trim(), "for")));
        return remainder.substring(endIndex+1).trim();
    }

    protected List<TypeListing> parseTypeListings(final String context, final String sepKeyWord) throws ParseException {
        final List<TypeListing> parsedListings = new LinkedList<>();
        int sepIndex = -1;
        int nextSepIndex = context.indexOf(sepKeyWord);
        int miniSepIndex = -1; // the commas
        int nextMiniSepIndex = -1;
        String excerpt = null;
        if (nextSepIndex == -1) {
            throw new ParseException("No '" + sepKeyWord + "' in the type listing statement!");
        }
        while (nextSepIndex != -1) {
            sepIndex = nextSepIndex;
            nextSepIndex = context.indexOf(sepKeyWord, sepIndex+sepKeyWord.length());
            nextMiniSepIndex = context.indexOf(",", sepIndex+sepKeyWord.length());
            if (nextMiniSepIndex > nextSepIndex) {
                throw new ParseException("Missing ',' before next '" + sepKeyWord + "'!");
            }
            // last entry
            if (nextMiniSepIndex == -1) {
                nextMiniSepIndex = context.length();
            }
            excerpt = context.substring(miniSepIndex+1, nextMiniSepIndex).trim();
            parsedListings.add(parseTypeListing(excerpt, sepKeyWord));
            miniSepIndex = nextMiniSepIndex;
        }
        return parsedListings;
    }

    protected TypeListing parseTypeListing(final String content, final String sepKeyWord) throws ParseException {
        String[] parts = content.split(sepKeyWord);
        if (parts.length != 2) {
            throw new ParseException("Type Listing '" + content + "' is missing key word '" + sepKeyWord + "'!");
        }
        return new TypeListing(parseTypeExpression(parts[1].trim()), parseStringList(parts[0].trim()));
    }

    protected TypeExpression parseTypeExpression(final String content) throws ParseException {
        int ofIndex = content.indexOf("of");
        RadixType radix = null;
        int modeIndex = -1;
        if (ofIndex == -1) {
            modeIndex = content.lastIndexOf(" ")+1;
            radix = new RadixType(content.substring(modeIndex), true);
        }
        else {
            modeIndex = content.lastIndexOf(" ", ofIndex-2)+1;
            radix = new RadixType(content.substring(modeIndex, ofIndex-1), true,
                    parseTermExpressionList(content.substring(ofIndex+3).trim()));
        }
        return new TypeExpression(radix, parseAdjectiveCluster(content.substring(0, modeIndex).trim()));
    }

    protected List<TermExpression> parseTermExpressionList(final String content) {
        // TODO implement correctly
        return Collections.emptyList();
    }

    protected List<Adjective> parseAdjectiveCluster(final String content) throws ParseException {
        if (content.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Adjective> parsedCluster = new LinkedList<>();
        String[] adjectiveCandidates = content.split("[ \n]");
        boolean negated = false;
        String candidate;
        for (int i = 0; i < adjectiveCandidates.length; i++) {
            candidate = adjectiveCandidates[i].trim();
            if (candidate.isEmpty()) {
                continue;
            }
            if (candidate.equals("non")) {
                if (negated) {
                    throw new ParseException("Can only negate attributes ones!");
                }
                negated = true;
            }
            else {
                parsedCluster.add(new Adjective(candidate, negated));
                negated = false;
            }
        }
        if (negated) {
            throw new ParseException("Missing attribute for 'non'!");
        }
        return parsedCluster;
    }

    protected List<String> parseStringList(final String content) {
        if (content.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(content.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty()).toList();
    }

}
