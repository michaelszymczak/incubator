package com.michaelszymczak.sample;

import java.util.ArrayList;
import java.util.List;


import static java.util.List.copyOf;
import static java.util.stream.IntStream.range;

class Diff
{
    private final List<String> a;
    private final List<String> b;

    public Diff(final List<String> a, final List<String> b)
    {
        this.a = copyOf(a);
        this.b = copyOf(b);
    }

    public Result result()
    {
        if (a.equals(b))
        {
            return new Result(0, a, b);
        }
        if (a.isEmpty() || b.isEmpty())
        {
            final int expectedLength = Math.abs(a.size() - b.size());
            final List<String> aResult = new ArrayList<>(a);
            final List<String> bResult = new ArrayList<>(b);
            range(aResult.size(), expectedLength).forEach(__ -> aResult.add(""));
            range(bResult.size(), expectedLength).forEach(__ -> bResult.add(""));

            return new Result(expectedLength, aResult, bResult);
        }

        final Result result1 = new Diff(a.subList(1, a.size()), b).result();
        final Result result2 = new Diff(a, b.subList(1, b.size())).result();
        if (result1.differences < result2.differences)
        {
            final List<String> newB = new ArrayList<>(List.of(""));
            newB.addAll(b);
            return new Result(1, a, newB);
        }
        else
        {
            final List<String> newA = new ArrayList<>(List.of(""));
            newA.addAll(result2.a);
            return new Result(result2.differences + 1, newA, b);
        }
    }
}
