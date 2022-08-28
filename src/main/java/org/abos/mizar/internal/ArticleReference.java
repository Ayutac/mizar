package org.abos.mizar.internal;

import java.util.Locale;
import java.util.Objects;

public class ArticleReference {

    private final String refS;

    public ArticleReference(String ref) {
        if (ref.length() > 8) {
            throw new IllegalArgumentException("Article names can only be up to 8 symbols long!");
        }
        if (!ref.equals(ref.toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Only UPPERCASE names are allowed!");
        }
        this.refS = ref;
    }

    /**
     * Returns the reference string
     * @return the reference string
     */
    public String getRefS() {
        return refS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleReference that = (ArticleReference) o;
        return Objects.equals(refS, that.refS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refS);
    }

    @Override
    public String toString() {
        return "ArticleReference[" + refS + ']';
    }
}
