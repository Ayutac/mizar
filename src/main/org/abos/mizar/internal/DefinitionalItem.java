package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record DefinitionalItem(List<DefinitionalPart> parts) implements TextItem {

    public DefinitionalItem(List<DefinitionalPart> parts) {
        this.parts = Collections.unmodifiableList(parts);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (DefinitionalPart part : parts) {
            part.checkSyntax(environ);
        }
    }

}
