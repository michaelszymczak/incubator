package com.michaelszymczak.sample;

import java.util.ArrayList;
import java.util.List;


import static java.util.stream.IntStream.range;

public class Lists
{
    static List<String> prepended(final String value, final List<String> original)
    {
        final List<String> prepended = new ArrayList<>(List.of(value));
        prepended.addAll(original);
        return prepended;
    }

    static List<String> appended(final List<String> original, final String value, final int totalLength)
    {
        final List<String> appended = new ArrayList<>(original);
        range(appended.size(), totalLength).forEach(__ -> appended.add(value));
        return appended;
    }
}
