package org.abos.mizar;

import org.abos.mizar.internal.ArticleReference;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Utils {

    public final static String MIZFILES = System.getenv("MIZFILES");

    public final static String MML_LAR = "mml.lar";

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

    public static String removeComments(final String content) {
        return content.replaceAll("::.*", "");
    }


}
