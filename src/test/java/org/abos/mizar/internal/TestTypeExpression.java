package org.abos.mizar.internal;

import org.abos.mizar.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestTypeExpression {

    @Test
    public void testSetExpression() throws IOException, SyntaxException, ParseException {
        Environ environ = TestEnviron.createEmptyEnviron();
        environ.load();
        TypeExpression setExpression = new TypeExpression(new RadixType("set", true));
        setExpression.checkSyntax(environ);
    }

    @Test
    public void testAbsentExpression() throws IOException, ParseException {
        Environ environ = TestEnviron.createEmptyEnviron();
        environ.load();
        TypeExpression objectExpression = new TypeExpression(new RadixType("object", true));
        try {
            objectExpression.checkSyntax(environ);
            Assertions.fail("object is not loaded in empty environ!");
        } catch (SyntaxException ex) {
            // expected
        }
    }

}
