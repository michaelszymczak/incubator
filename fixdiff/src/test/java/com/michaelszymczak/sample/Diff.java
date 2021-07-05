package com.michaelszymczak.sample;

import java.util.List;
import java.util.function.Function;


import static com.michaelszymczak.sample.Lists.appended;
import static com.michaelszymczak.sample.Lists.prepended;
import static java.util.List.copyOf;
import static java.util.stream.Collectors.toList;

public final class Diff<Value>
{
    private final String emptyLabel;
    private final Value emptyValue;
    private final Function<Value, String> toLabel;
    private final List<String> aLabels;
    private final List<String> bLabels;
    private final List<Value> aValues;
    private final List<Value> bValues;

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
        this.aLabels = aValues.stream().map(toLabel).collect(toList());
        this.bLabels = bValues.stream().map(toLabel).collect(toList());
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

        final Result<Value> bShiftedRightResult = new Diff<>(emptyLabel, emptyValue, toLabel, aValues.subList(1, aValues.size()), bValues).result();
        final Result<Value> aShiftedRightResult = new Diff<>(emptyLabel, emptyValue, toLabel, aValues, bValues.subList(1, bValues.size())).result();
        if (aLabels.get(0).equals(bLabels.get(0)))
        {
            final Result<Value> matchingFirstLetterResult = new Diff<>(emptyLabel, emptyValue, toLabel, aValues.subList(1, aValues.size()), bValues.subList(1, bValues.size())).result();
            if (matchingFirstLetterResult.differences() < bShiftedRightResult.differences() + 1 && matchingFirstLetterResult.differences() < aShiftedRightResult.differences())
            {
                return new Result<>(
                        matchingFirstLetterResult.differences(),
                        prepended(aLabels.get(0), matchingFirstLetterResult.aLabels),
                        prepended(bLabels.get(0), matchingFirstLetterResult.bLabels),
                        prepended(aValues.get(0), matchingFirstLetterResult.aValues),
                        prepended(bValues.get(0), matchingFirstLetterResult.bValues)
                );
            }
        }
        if (bShiftedRightResult.differences() < aShiftedRightResult.differences())
        {
            return new Result<>(
                    bShiftedRightResult.differences() + 1,
                    prepended(aLabels.get(0), bShiftedRightResult.aLabels),
                    prepended(emptyLabel, bShiftedRightResult.bLabels),
                    prepended(aValues.get(0), bShiftedRightResult.aValues),
                    prepended(emptyValue, bShiftedRightResult.bValues)
            );
        }
        else
        {
            return new Result<>(
                    aShiftedRightResult.differences() + 1,
                    prepended(emptyLabel, aShiftedRightResult.aLabels),
                    prepended(bLabels.get(0), aShiftedRightResult.bLabels),
                    prepended(emptyValue, aShiftedRightResult.aValues),
                    prepended(bValues.get(0), aShiftedRightResult.bValues)
            );
        }
    }
}
