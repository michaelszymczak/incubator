package com.michaelszymczak.sample;

import java.util.List;


import static java.util.List.copyOf;

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
        return new Result(Math.abs(a.size() - b.size()));
    }
}
