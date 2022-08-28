package org.abos.mizar.parser;

import org.abos.mizar.Utils;
import org.abos.mizar.internal.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ArticleParser {

    public static final Collection<String> END_STARTERS = Stream.concat(
            Arrays.stream(TextItemType.values()).map(TextItemType::getKeyword),
            Stream.of("proof", "now", "hereby", "suppose")
    ).toList();

    public static final Pattern END_STARTERS_PATTERN = Pattern.compile(String.join("|", END_STARTERS)+"\\s");

    public static final Pattern END_PATTERN = Pattern.compile("end\\s*;");

    public static final Pattern BEGIN_PATTERN = Pattern.compile("begin\\s");

    public static final Pattern LABEL_PATTERN = Pattern.compile("[A-Za-z]\\w*:");

    public static final Collection<String> CORRECTNESS_TYPES = Arrays.stream(CorrectnessConditionType.values()).map(CorrectnessConditionType::getName).toList();

    public static final Pattern CORRECTNESS_TYPES_PATTERN = Pattern.compile(String.join("|", CORRECTNESS_TYPES));

    protected Future<Environ> environ;

    public Article parse(final String name, final String content) throws ParseException, ExecutionException, InterruptedException {
        final ArticleReference artName = new ArticleReference(name);
        final StringWrapper remainder = new StringWrapper(Utils.removeComments(content));
        final int firstBeginIndex = remainder.indexOf("begin");
        if (firstBeginIndex == -1) {
            throw new ParseException("No 'begin' in article!");
        }
        final String environPlusStr = remainder.getString();
        environ = Executors.newSingleThreadExecutor().submit(() -> {
                Environ e = parseEnviron(environPlusStr.substring(0, firstBeginIndex).trim());
                e.load();
                return e;
        });

        final List<TextItem> textItems = new LinkedList<>();
        remainder.substring(firstBeginIndex+5).trim();
        while (!remainder.isEmpty()) {
            if (remainder.startsWith(BEGIN_PATTERN)) {
                remainder.substring(5);
            }
            else if (remainder.startsWith(TextItemType.RESERVATION.getPattern())) {
                // remove 'reserve'
                textItems.add(parseReservation(remainder.substring(TextItemType.RESERVATION.length()).trim()));
            }
            else if (remainder.startsWith(TextItemType.THEOREM.getPattern())) {
                // remove 'theorem'
                textItems.add(parseCompactStatement(remainder.substring(TextItemType.THEOREM.length()).trim(), true));
            }
            else if (remainder.startsWith(TextItemType.DEFINITIONAL.getPattern())) {
                textItems.add(parseDefinitional(remainder));
            }
            // TODO parse remaining text items
            else {
                throw new ParseException("Unknown remainder!");
            }
        }
        return new Article(artName, environ.get(), textItems);
    }

    protected Environ parseEnviron(final String environContent) throws ParseException {
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

    protected Reservation parseReservation(final StringWrapper remainder) throws ParseException {
        String excerpt = remainder.getString();
        int endIndex = excerpt.indexOf(';');
        if (endIndex == -1) {
            throw new ParseException("No end of '" + TextItemType.RESERVATION.getKeyword() + "'!");
        }
        remainder.substring(endIndex+1).trim();
        return new Reservation(parseTypeListings(excerpt.substring(0, endIndex).trim(), "for"));
    }

    protected CompactStatement parseCompactStatement(final StringWrapper remainder, boolean theorem) throws ParseException {
        Utils.IntTriple justificationIndices = findJustificationIndices(remainder.getString());
        // parse the label
        String propositionStr = remainder.getString().substring(0, justificationIndices.start());
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
        if (justificationIndices.middle() != -1) {
            just = parseJustification(remainder.getString().substring(justificationIndices.start()+5, justificationIndices.middle()));
        }
        remainder.substring(justificationIndices.end()).trim();
        return new CompactStatement(proposition, just, theorem);
    }

    protected DefinitionalItem parseDefinitional(final StringWrapper remainder) throws ParseException {
        final List<DefinitionalPart> parts = new LinkedList<>();
        final Utils.IntPair defEnd = findEndIndex(remainder.getString(), TextItemType.DEFINITIONAL.getKeyword());
        StringWrapper excerpt = new StringWrapper(remainder.getString().substring(TextItemType.DEFINITIONAL.length(), defEnd.start()).trim());
        int sepIndex = -1;
        while (!excerpt.isEmpty()) {
            sepIndex = excerpt.indexOf(';');
            if (sepIndex == -1) {
                throw new ParseException("Missing ';'!");
            }
            if (excerpt.startsWith("let")) {
                parts.add(parseGeneralization(excerpt.getString().substring(3, sepIndex).trim()));
                excerpt.substring(sepIndex+1).trim();
            }
            else if (excerpt.startsWith("assume")) {
                parts.add(parseAssumption(excerpt.getString().substring(6, sepIndex).trim()));
                excerpt.substring(sepIndex+1).trim();
            }
            // TODO parse auxiliary item
            else {
                boolean redefine = excerpt.startsWith(Definition.REDEFINE);
                if (redefine) {
                    excerpt = excerpt.substring(Definition.REDEFINE.length()).trim();
                }
                if (excerpt.startsWith(Definition.ATTRIBUTE)) {
                    parts.add(parseAttributeDef(excerpt.substring(Definition.ATTRIBUTE.length()), redefine));
                }
                else if (excerpt.startsWith(Definition.FUNCTOR)) {
                    parts.add(parseFunctorDef(excerpt.substring(Definition.ATTRIBUTE.length()), redefine, parts));
                }
                else if (excerpt.startsWith(Definition.MODE)) {
                    parts.add(parseModeDef(excerpt.substring(Definition.ATTRIBUTE.length()), redefine, parts));
                }
                else if (excerpt.startsWith(Definition.PREDICATE)) {
                    parts.add(parsePredicateDef(excerpt.substring(Definition.ATTRIBUTE.length()), redefine, parts));
                }
                else if (excerpt.startsWith(Definition.STRUCTURE)) {
                    parts.add(parseStructureDef(excerpt.substring(Definition.ATTRIBUTE.length()), parts));
                }
                else {
                    throw new ParseException("Unknown definition type!");
                }
            }
        }
        if (defEnd.end() == remainder.length()) {
            remainder.setString("");
        }
        else {
            remainder.substring(defEnd.end()+1).trim();
        }
        return new DefinitionalItem(parts);
    }

    protected AttributeDefinition parseAttributeDef(final StringWrapper remainder, boolean redefine) throws ParseException {
        int sepIndex = remainder.indexOf("is");
        if (sepIndex == -1) {
            throw new ParseException("Missing 'is'!");
        }
        final String attrVariable = remainder.getString().substring(0, sepIndex).trim();
        remainder.substring(sepIndex+2).trim();
        sepIndex = remainder.indexOf("means");
        if (sepIndex == -1) {
            throw new ParseException("Missing 'means'!");
        }
        final AttributePattern pattern = new AttributePattern(remainder.getString().substring(0,sepIndex).trim(), attrVariable);
        remainder.substring(sepIndex+5).trim();
        sepIndex = remainder.indexOf(';');
        if (sepIndex == -1) {
            throw new ParseException("Missing ';'");
        }

        final Definiens definiens = parseDefiniens(remainder.getString().substring(0,sepIndex).trim());
        remainder.substring(sepIndex+1).trim();
        final CorrectnessConditions conditions = parseCorrectnessConditions(remainder);
        return new AttributeDefinition(pattern, definiens, conditions, redefine);
    }

    protected FunctorDefinition parseFunctorDef(final StringWrapper remainder, boolean redefine, List<DefinitionalPart> parts) throws ParseException {
        // TODO implement
        return null;
    }

    protected ModeDefinition parseModeDef(final StringWrapper remainder, boolean redefine, List<DefinitionalPart> parts) throws ParseException {
        // TODO implement
        return null;
    }

    protected PredicateDefinition parsePredicateDef(final StringWrapper remainder, boolean redefine, List<DefinitionalPart> parts) throws ParseException {
        int sepIndex = remainder.indexOf("means");
        if (sepIndex == -1) {
            throw new ParseException("Missing 'means'!");
        }
        final PredicatePattern pattern = parsePredicatePattern(remainder.getString().substring(0, sepIndex).trim());
        remainder.substring(sepIndex+5).trim();
        final Definiens definiens = parseDefiniens(remainder.getString().substring(0,sepIndex).trim());
        remainder.substring(sepIndex+1).trim();
        final CorrectnessConditions conditions = parseCorrectnessConditions(remainder);
        final List<PredicateProperty> properties = new LinkedList<>();
        // TODO parse predicate properties
        return new PredicateDefinition(pattern, definiens, conditions, properties, redefine);
    }

    protected PredicatePattern parsePredicatePattern(final String context) throws ParseException, IllegalStateException {
        String[] parts = context.split(" ");
        String candidate = null;
        String predicate = null;
        try {
            for (final String part : parts) {
                candidate = part.trim();
                if (environ.get().isValidSymbol(candidate, VocabularySymbols.R)) {
                    predicate = candidate;
                    break;
                }
            }
        }
        catch (ExecutionException | InterruptedException ex) {
            throw new IllegalArgumentException("environ wasn't loaded properly!", ex);
        }
        if (predicate == null) {
            throw new ParseException("Predicate symbol expected!");
        }
        int index = context.indexOf(predicate);
        String lociRaw = context.substring(0, index);
        List<String> prefixes;
        if (index != 0) {
            prefixes = Arrays.stream(lociRaw.split(",")).map(String::trim).toList();
        }
        else {
            prefixes = Collections.emptyList();
        }
        index += predicate.length();
        List<String> suffixes;
        if (index != context.length()) {
            lociRaw = context.substring(index+1);
            suffixes = Arrays.stream(lociRaw.split(",")).map(String::trim).toList();
        }
        else {
            suffixes = Collections.emptyList();
        }
        return new PredicatePattern(predicate, prefixes, suffixes);
    }

    protected StructureDefinition parseStructureDef(final StringWrapper remainder, List<DefinitionalPart> parts) throws ParseException {
        // TODO implement
        return null;
    }

    protected Definiens parseDefiniens(final String context) {
        // TODO implement
        return null;
    }

    protected CorrectnessConditions parseCorrectnessConditions(final StringWrapper remainder) throws ParseException {
        final List<CorrectnessCondition> conditions = new LinkedList<>();
        Matcher correctnessMatcher = null;
        while (true) {
            correctnessMatcher = CORRECTNESS_TYPES_PATTERN.matcher(remainder.getString());
            if (correctnessMatcher.find() && correctnessMatcher.start() == 0) {
                parseSpecificCorrectnessCondition(remainder, conditions);
            }
            else {
                break;
            }
        }
        // parse the correctness justification
        Justification correctness = null;
        if (remainder.startsWith("correctness")) {
            Utils.IntTriple justIndices = findJustificationIndices(remainder.getString());
            if (justIndices.middle() != -1) {
                correctness = parseJustification(remainder.getString().substring(justIndices.start()+5, justIndices.middle()));
            }
            remainder.substring(justIndices.end()+1);
        }
        return new CorrectnessConditions(conditions, correctness);
    }

    protected void parseSpecificCorrectnessCondition(final StringWrapper remainder, List<CorrectnessCondition> conditions) throws ParseException {
        // TODO implement
    }

    protected Generalization parseGeneralization(final String context) throws ParseException {
        // TODO add parsing of conditions
        return new Generalization(parseTypeListings(context, "be"));
    }

    protected Assumption parseAssumption(final String context) {
        // TODO implement
        return null;
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

    protected Utils.IntTriple findJustificationIndices(final String context) throws ParseException {
        // find the end of the justification
        int noJustIndex = context.indexOf(';');
        int justIndex = context.indexOf("proof");
        int justStartIndex = -1;
        int justBeforeEndIndex = -1;
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
            justificationEnd = findEndIndex(context.substring(justIndex), "proof");
            justBeforeEndIndex = justIndex+justificationEnd.start();
            justEndIndex = justIndex+justificationEnd.end();
        }
        return new Utils.IntTriple(justStartIndex, justBeforeEndIndex, justEndIndex);
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
            // if there are no more starters, we must find an end
            if (!startMatcher.find()) {
                matchEnd = true;
            }
            // if there is another starter before the next end, increase count
            if (!matchEnd && startMatcher.start() < endMatcher.start()) {
                matchEnd = false;
                count++;
            }
            // else the next starter is after the next end, decrease count
            else {
                count--;
            }
            // if we need to match an end, but there are no more left, throw parse exception
            if (matchEnd && count > 0 && !endMatcher.find()) {
                throw new ParseException("*214 Not enough 'end's but " + count + " more are needed!");
            }
        }
        return new Utils.IntPair(offset+endMatcher.start(), offset+endMatcher.end());
    }

}
