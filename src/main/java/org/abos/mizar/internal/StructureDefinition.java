package org.abos.mizar.internal;

import java.util.Collections;
import java.util.List;

public record StructureDefinition(String name, Ancestors ancestors, List<String> variables, List<FieldSegment> fields) implements Definition {

    public StructureDefinition(String name, Ancestors ancestors, List<String> variables, List<FieldSegment> fields) {
        this.name = name;
        this.ancestors = ancestors;
        this.variables = Collections.unmodifiableList(variables);
        this.fields = Collections.unmodifiableList(fields);
    }

    @Override
    public void checkSyntax(Environ environ) throws SyntaxException {
        if (!environ.isValidSymbol(name, VocabularySymbols.G)) {
            throw new SyntaxException("*104 Unknown structure " + name);
        }
        ancestors.checkSyntax(environ);
        for (FieldSegment fieldSegment : fields) {
            fieldSegment.checkSyntax(environ);
        }
    }

    @Override
    public boolean redefinition() {
        return false;
    }
}
