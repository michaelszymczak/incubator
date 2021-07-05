package com.michaelszymczak.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;


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

    static <Value> boolean equal(final BiPredicate<Value, Value> isEqual, final List<Value> a, final List<Value> b)
    {
        return a.size() == b.size() && range(0, a.size()).allMatch(i -> isEqual.test(a.get(i), b.get(i)));
    }

    static <Value> List<String> map(final Function<Value, String> mapping, final List<Value> values)
    {
        return values.stream().map(mapping).collect(Collectors.toList());
    }
}
