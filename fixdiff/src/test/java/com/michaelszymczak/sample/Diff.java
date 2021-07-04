package com.michaelszymczak.sample;

import java.util.List;


import static com.michaelszymczak.sample.Lists.appended;
import static com.michaelszymczak.sample.Lists.prepended;
import static java.util.List.copyOf;

class Diff
{
    private final String emptyValue;
    private final List<String> a;
    private final List<String> b;

    public Diff(final List<String> a, final List<String> b)
    {
        this("_", a, b);
    }

    public Diff(final String emptyValue, final List<String> a, final List<String> b)
    {
        this.emptyValue = emptyValue;
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
            return new Result(expectedLength, appended(a, emptyValue, expectedLength), appended(b, emptyValue, expectedLength));
        }

        final Result result1 = new Diff(a.subList(1, a.size()), b).result();
        final Result result2 = new Diff(a, b.subList(1, b.size())).result();
        if (a.get(0).equals(b.get(0)))
        {
            final Result result3 = new Diff(a.subList(1, a.size()), b.subList(1, b.size())).result();
            if (result3.differences() < result1.differences() + 1 && result3.differences() < result2.differences())
            {
                return new Result(result3.differences(), prepended(a.get(0), result3.a), prepended(b.get(0), result3.b));
            }
        }
        if (result1.differences() < result2.differences())
        {
            return new Result(result1.differences() + 1, prepended(a.get(0), result1.a), prepended(emptyValue, result1.b));
        }
        else
        {
            return new Result(result2.differences() + 1, prepended(emptyValue, result2.a), prepended(b.get(0), result2.b));
        }
    }

}
