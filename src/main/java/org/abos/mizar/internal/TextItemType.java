package org.abos.mizar.internal;

import java.util.regex.Pattern;

public enum TextItemType {

    RESERVATION("reserve"),

    DEFINITIONAL("definition"),

    REGISTRATION("registration"),

    NOTATION("notation"),

    THEOREM("theorem"),

    SCHEME("scheme");

    private final String keyword;

    private final Pattern pattern;

    TextItemType(String keyword) {
        this.keyword = keyword;
        this.pattern = Pattern.compile(keyword+"\\s");
    }

    public String getKeyword() {
        return keyword;
    }

    public int length() {
        return keyword.length();
    }

    public Pattern getPattern() {
        return pattern;
    }
}
