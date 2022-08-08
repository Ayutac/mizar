package org.abos.mizar.core;

import java.util.Objects;

public class ArticleReference {

    private final String refS;

    public ArticleReference(String ref) {
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
}
