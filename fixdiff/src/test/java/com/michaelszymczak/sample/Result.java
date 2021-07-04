package com.michaelszymczak.sample;

import java.util.List;

class Result<Value>
{
    final List<Value> a;
    final List<Value> b;
    private final int differences;

    public Result(final int differences, final List<Value> a, final List<Value> b)
    {
        this.differences = differences;
        this.a = a;
        this.b = b;
    }

    public int differences()
    {
        return differences;
    }

    @Override
    public int hashCode()
    {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + differences;
        return result;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final Result<?> result = (Result<?>)o;

        if (differences != result.differences)
        {
            return false;
        }
        if (a != null ? !a.equals(result.a) : result.a != null)
        {
            return false;
        }
        return b != null ? b.equals(result.b) : result.b == null;
    }

    @Override
    public String toString()
    {
        return "Result{" +
               "a=" + a +
               ", b=" + b +
               ", differences=" + differences +
               '}';
    }
}
