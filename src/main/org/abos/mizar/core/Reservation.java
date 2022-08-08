package org.abos.mizar.core;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Reservation implements TextItem {

    private final List<TypeListing> listings;

    public Reservation(List<TypeListing> listings) {
        this.listings = Collections.unmodifiableList(listings);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (TypeListing listing : listings) {
            listing.checkSyntax(environ);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(listings, that.listings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listings);
    }
}
