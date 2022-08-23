package org.abos.mizar.internal;

public interface Definition extends DefinitionalPart {

    String ATTRIBUTE = "attr";

    String FUNCTOR = "func";

    String MODE = "mode";

    String PREDICATE = "pred";

    String STRUCTURE = "struct";

    String REDEFINE = "redefine";

    boolean redefinition();

}
