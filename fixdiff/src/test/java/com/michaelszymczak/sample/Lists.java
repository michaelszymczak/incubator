package com.michaelszymczak.sample;

import java.util.ArrayList;
import java.util.List;


import static java.util.stream.IntStream.range;

public class Lists
{
    static <Value> List<Value> prepended(final Value value, final List<Value> original)
    {
        final List<Value> prepended = new ArrayList<>(List.of(value));
        prepended.addAll(original);
        return prepended;
    }

    static <Value> List<Value> appended(final List<Value> original, final Value value, final int totalLength)
    {
        final List<Value> appended = new ArrayList<>(original);
        range(appended.size(), totalLength).forEach(__ -> appended.add(value));
        return appended;
    }
}
