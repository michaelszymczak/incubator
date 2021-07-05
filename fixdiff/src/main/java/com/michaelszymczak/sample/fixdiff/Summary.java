package com.michaelszymczak.sample.fixdiff;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Summary
{
    private final int numberOfCompletelyDifferentItems;
    private final List<Entry> entries;
    private final String labelsA;
    private final String labelsB;

    public Summary(Result<?> result)
    {
        this.numberOfCompletelyDifferentItems = result.differences();
        labelsA = String.join("\n", result.aLabels);
        labelsB = String.join("\n", result.bLabels);
        entries = IntStream.range(0, result.entriesCount()).mapToObj(i -> new Entry(
                result.aValues.get(i).toString(), result.bValues.get(i).toString()
        )).collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return "Summary{" +
               "numberOfCompletelyDifferentItems=" + numberOfCompletelyDifferentItems() +
               ", entries=" + entries() +
               ", labelsA='" + labelsA() + '\'' +
               ", labelsB='" + labelsB + '\'' +
               '}';
    }

    public int numberOfCompletelyDifferentItems()
    {
        return numberOfCompletelyDifferentItems;
    }

    public List<Entry> entries()
    {
        return entries;
    }

    public String labelsA()
    {
        return labelsA;
    }

    public String labelsB()
    {
        return labelsB;
    }

    public static class Entry
    {
        public final String a;
        public final String b;

        public Entry(final String a, final String b)
        {
            this.a = a;
            this.b = b;
        }
    }
}
