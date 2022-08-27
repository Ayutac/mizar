package org.abos.mizar;

import org.abos.mizar.internal.ArticleReference;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    public final static String MIZFILES = System.getenv("MIZFILES");

    public final static String MML_LAR = "mml.lar";

    public final static String MML_VCT = "mml.vct";

    public record IntPair(int start, int end) {}

    public record IntTriple(int start, int middle, int end) {}

    private Utils() {
        // no instantiation
    }

    public static String loadFromResource(final String file) throws IOException {
        URL url = Utils.class.getResource(file);
        if (url == null) {
            throw new NoSuchFileException(file + "not found!");
        }
        return Files.readString(Path.of(url.getPath()));
    }

    public static String loadFromMizar(final String file) throws IOException {
        return Files.readString(Path.of(MIZFILES, file));
    }

    public static List<ArticleReference> loadAllArticleNames() throws IOException {
        return Files.readAllLines(Path.of(MIZFILES, MML_LAR)).stream()
                .map(art -> art.toUpperCase(Locale.ROOT))
                .map(ArticleReference::new).collect(Collectors.toList());
    }

    public static List<String> loadVocabularyFile() throws IOException {
        return Files.readAllLines(Path.of(MIZFILES, MML_VCT));
    }

    public static String removeComments(final String content) {
        return content.replaceAll("::.*", "");
    }

    public static Comparator<ArticleReference> getLexicalArticleComparator() {
        return (o1, o2) -> {
            if (o1 == o2) {
                return 0;
            }
            if (o1 == null) {
                return Integer.MIN_VALUE;
            }
            if (o2 == null) {
                return Integer.MAX_VALUE;
            }
            return o1.getRefS().compareTo(o2.getRefS());
        };
    }

}
