package org.example.wwtbam.model;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public final class NormalizeAnswer{

    private static final Pattern ACCENT_MARKS = Pattern.compile("\\p{M}+");
    private static final Pattern SEPARATORS = Pattern.compile("[^\\p{L}\\p{N}-]+");
    private static final Pattern LEADING_ARTICLE = Pattern.compile("^(the|a|an) ");

    private NormalizeAnswer() {

    }

    public static String normalize(String input) {
        String s = Normalizer.normalize(input, Normalizer.Form.NFD);
        s = ACCENT_MARKS.matcher(s).replaceAll("");
        s = s.toLowerCase(Locale.ROOT).replace('−', '-');
        s = SEPARATORS.matcher(s).replaceAll(" ").trim();
        return LEADING_ARTICLE.matcher(s).replaceFirst("");
    }

}