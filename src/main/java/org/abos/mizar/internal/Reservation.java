package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record Reservation(List<TypeListing> listings) implements TextItem {

    public Reservation(List<TypeListing> listings) {
        this.listings = Collections.unmodifiableList(listings);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (TypeListing listing : listings) {
            listing.checkSyntax(environ);
        }
    }
}
