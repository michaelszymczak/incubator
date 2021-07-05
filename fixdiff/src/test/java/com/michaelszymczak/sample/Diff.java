package com.michaelszymczak.sample;

import java.util.List;
import java.util.function.Function;


import static com.michaelszymczak.sample.Lists.appended;
import static com.michaelszymczak.sample.Lists.map;
import static com.michaelszymczak.sample.Lists.prepended;
import static java.util.List.copyOf;

public final class Diff<Value>
{
    private final Value emptyValue;
    private final List<Value> aValues;
    private final List<Value> bValues;
    private final List<String> aLabels;
    private final List<String> bLabels;
    private final Function<Value, String> toLabel;
    private final String emptyLabel;

    private Diff(
            final String emptyLabel,
            final Value emptyValue,
            final Function<Value, String> toLabel,
            final List<Value> aValues,
            final List<Value> bValues
    )
    {
        this.emptyLabel = emptyLabel;
        this.emptyValue = emptyValue;
        this.aValues = copyOf(aValues);
        this.bValues = copyOf(bValues);
        this.aLabels = map(toLabel, aValues);
        this.bLabels = map(toLabel, bValues);
        this.toLabel = toLabel;
        if (aLabels.stream().anyMatch(value -> value.equals(emptyLabel)) || bLabels.stream().anyMatch(value -> value.equals(emptyLabel)))
        {
            throw new IllegalArgumentException("Empty label " + emptyLabel + " not allowed in the sequences");
        }
    }

    public static Diff<String> diff(final String emptyValue, final List<String> a, final List<String> b)
    {
        return new Diff<>(emptyValue, emptyValue, Object::toString, a, b);
    }

    public static <Value> Diff<Value> diff(
            final String emptyLabel,
            final Value emptyValue,
            final Function<Value, String> toLabel,
            final List<Value> a,
            final List<Value> b
    )
    {
        return new Diff<>(emptyLabel, emptyValue, toLabel, a, b);
    }

    public Result<Value> result()
    {
        if (aLabels.equals(bLabels))
        {
            return new Result<>(0, aLabels, bLabels, aValues, bValues);
        }
        if (aLabels.isEmpty() || bLabels.isEmpty())
        {
            final int expectedLength = Math.abs(aValues.size() - bValues.size());
            return new Result<>(
                    expectedLength,
                    appended(aLabels, emptyLabel, expectedLength),
                    appended(bLabels, emptyLabel, expectedLength),
                    appended(aValues, emptyValue, expectedLength),
                    appended(bValues, emptyValue, expectedLength)
            );
        }

        final Result<Value> result1 = new Diff<>(emptyLabel, emptyValue, toLabel, aValues.subList(1, aValues.size()), bValues).result();
        final Result<Value> result2 = new Diff<>(emptyLabel, emptyValue, toLabel, aValues, bValues.subList(1, bValues.size())).result();
        if (aLabels.get(0).equals(bLabels.get(0)))
        {
            final Result<Value> result3 = new Diff<>(emptyLabel, emptyValue, toLabel, aValues.subList(1, aValues.size()), bValues.subList(1, bValues.size())).result();
            if (result3.differences() < result1.differences() + 1 && result3.differences() < result2.differences())
            {
                return new Result<>(
                        result3.differences(),
                        prepended(aLabels.get(0), result3.aLabels),
                        prepended(bLabels.get(0), result3.bLabels),
                        prepended(aValues.get(0), result3.aValues),
                        prepended(bValues.get(0), result3.bValues)
                );
            }
        }
        if (result1.differences() < result2.differences())
        {
            return new Result<>(
                    result1.differences() + 1,
                    prepended(aLabels.get(0), result1.aLabels),
                    prepended(emptyLabel, result1.bLabels),
                    prepended(aValues.get(0), result1.aValues),
                    prepended(emptyValue, result1.bValues)
            );
        }
        else
        {
            return new Result<>(
                    result2.differences() + 1,
                    prepended(emptyLabel, result2.aLabels),
                    prepended(bLabels.get(0), result2.bLabels),
                    prepended(emptyValue, result2.aValues),
                    prepended(bValues.get(0), result2.bValues)
            );
        }
    }

}
