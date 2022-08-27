package org.abos.mizar.parser;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringWrapper implements CharSequence {

    private String string;

    public StringWrapper(String string) {
        Objects.requireNonNull(string);
        this.string = string;
    }

    public StringWrapper() {
        this("");
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public StringWrapper substring(int beginIndex, int endIndex) {
        string = string.substring(beginIndex, endIndex);
        return this;
    }

    public StringWrapper substring(int beginIndex) {
        string = string.substring(beginIndex);
        return this;
    }

    public StringWrapper trim() {
        string = string.trim();
        return this;
    }

    public boolean startsWith(String str) {
        return string.startsWith(str);
    }

    public boolean startsWith(Pattern pattern) {
        Matcher matcher = pattern.matcher(string);
        return matcher.find() && matcher.start() == 0;
    }

    public int indexOf(int ch) {
        return string.indexOf(ch);
    }

    public int indexOf(String str) {
        return string.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return string.indexOf(str, fromIndex);
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @NotNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return new StringWrapper(string.substring(start, end));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringWrapper that = (StringWrapper) o;
        return Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }

    @Override
    public String toString() {
        return string;
    }
}
