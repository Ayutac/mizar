package org.abos.mizar.parser;

import org.abos.mizar.Utils;
import org.abos.mizar.internal.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {

    public static final Collection<String> END_STARTERS = Collections.unmodifiableCollection(Arrays.asList(
            "proof", "now", "hereby", "suppose"
    ));

    public static final Pattern END_STARTERS_PATTERN = Pattern.compile(String.join("|", END_STARTERS));

    public static final Pattern END_PATTERN = Pattern.compile("end\\s*;");

    public static final Pattern LABEL_PATTERN = Pattern.compile("[A-Za-z]\\w*:");

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
            if (remainder.startsWith(TextItem.THEOREM)) {
                remainder = parseCompactStatement(remainder.substring(TextItem.THEOREM.length()).trim(), textItems);
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
        int endIndex = remainder.indexOf(';');
        if (endIndex == -1) {
            throw new ParseException("No end of '" + TextItem.RESERVATION + "'!");
        }
        textItems.add(new Reservation(parseTypeListings(remainder.substring(0, endIndex).trim(), "for")));
        return remainder.substring(endIndex+1).trim();
    }

    protected String parseCompactStatement(final String remainder, final List<TextItem> textItems) throws ParseException {
        // find the end of the theorem
        int noJustIndex = remainder.indexOf(';');
        int justIndex = remainder.indexOf("proof");
        int justStartIndex = -1;
        int justEndIndex = -1;
        Utils.IntPair justificationEnd = null;
        if (noJustIndex != -1 && (justIndex == -1 || noJustIndex < justIndex)) {
            justStartIndex = noJustIndex;
            justEndIndex = justStartIndex+1;
        }
        else if (justIndex == -1) {
            throw new ParseException("*395 Justification expected!");
        }
        else {
            justStartIndex = justIndex;
            justificationEnd = findEndIndex(remainder.substring(justIndex), "proof");
            justEndIndex = justIndex+justificationEnd.end();
        }
        // parse the label
        String propositionStr = remainder.substring(0, justStartIndex);
        String formula = propositionStr;
        String label = null;
        Matcher labelMatcher = LABEL_PATTERN.matcher(propositionStr);
        if (labelMatcher.find() && labelMatcher.start() == 0) {
            label = labelMatcher.group();
            // remove trailing :
            label = label.substring(0, label.length()-1);
            formula = propositionStr.substring(labelMatcher.end());
        }
        // parse the formula expression
        Proposition proposition = new Proposition(parseFormulaExpression(formula.trim()), label);
        // parse the justification
        Justification just = null;
        if (justificationEnd != null) {
            just = parseJustification(remainder.substring(justStartIndex+5, justIndex+justificationEnd.start()));
        }
        textItems.add(new CompactStatement(proposition, just));
        return remainder.substring(justEndIndex).trim();
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

    protected List<TermExpression> parseTermExpressionList(final String content) throws ParseException {
        // TODO implement correctly
        return Collections.emptyList();
    }

    protected FormulaExpression parseFormulaExpression(final String content) throws ParseException {
        // TODO implement correctly
        return null;
    }

    protected Justification parseJustification(final String content) throws ParseException {
        // TODO implement correctly
        return null;
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

    protected Utils.IntPair findEndIndex(final String context, final String starter) throws ParseException {
        if (!END_STARTERS.contains(starter)) {
            throw new IllegalArgumentException("'" + starter + "' cannot close with 'end'!");
        }
        final int startIndex = context.indexOf(starter);
        if (startIndex == -1) {
            throw new IllegalArgumentException("'" + starter + "' doesn't appear within context!");
        }
        String excerpt = context.substring(startIndex+starter.length()).trim();
        int offset = context.indexOf(excerpt);
        Matcher startMatcher = END_STARTERS_PATTERN.matcher(excerpt);
        Matcher endMatcher = END_PATTERN.matcher(excerpt);
        if (!endMatcher.find()) {
            throw new ParseException("*214 No 'end' in context!");
        }
        boolean matchEnd = false;
        int count = 1;
        while (count > 0) {
            // note that the positions of 'find()' in the ifs are extremely important and sensitive to changes
            if (!startMatcher.find()) {
                matchEnd = true;
            }
            else if (startMatcher.start() < endMatcher.start()) {
                matchEnd = false;
                count++;
            }
            if (matchEnd && !endMatcher.find()) {
                throw new ParseException("*214 Not enough 'end's but " + count + "are needed!");
            }
            else {
                matchEnd = false;
                count--;
            }
        }
        return new Utils.IntPair(offset+endMatcher.start(), offset+endMatcher.end());
    }

}
