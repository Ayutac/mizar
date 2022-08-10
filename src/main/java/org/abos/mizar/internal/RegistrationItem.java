package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record RegistrationItem(List<RegistrationPart> parts) implements TextItem {

    public RegistrationItem(List<RegistrationPart> parts) {
        this.parts = Collections.unmodifiableList(parts);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        for (RegistrationPart part : parts) {
            part.checkSyntax(environ);
        }
    }

}
