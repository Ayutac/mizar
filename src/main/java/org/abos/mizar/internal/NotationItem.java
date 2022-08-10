package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record NotationItem(List<NotationPart> parts) implements TextItem {

    public NotationItem(List<NotationPart> parts) {
        this.parts = Collections.unmodifiableList(parts);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (NotationPart part : parts) {
            part.checkSyntax(environ);
        }
    }

}
