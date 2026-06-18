package com.metrowest.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextValidator
{ private TextValidator(){}

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9 ]+$");

    public static boolean alphanumeric_w_spaces(String text)
    {
        return Optional.ofNullable(text)
            .map(PATTERN::matcher)
            .map(Matcher::matches)
            .orElse(false);
    }
}
