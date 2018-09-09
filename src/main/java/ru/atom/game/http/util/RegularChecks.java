package ru.atom.game.http.util;

import java.util.regex.Pattern;

public class RegularChecks {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-_\\.]{2,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9-_\\.]{6,16}$");

    public static boolean isNameCorrect(String name) {
        return matchesWithPattern(NAME_PATTERN, name);
    }

    public static boolean isPasswordCorrect(String password) {
        return matchesWithPattern(PASSWORD_PATTERN, password);
    }

    private static boolean matchesWithPattern(Pattern pattern, String value) {
        return pattern.matcher(value).matches();
    }

}
