package com.michaelszymczak.sample;

import java.util.List;
import java.util.function.BiPredicate;


import static com.michaelszymczak.sample.Lists.appended;
import static com.michaelszymczak.sample.Lists.equal;
import static com.michaelszymczak.sample.Lists.prepended;
import static java.util.List.copyOf;

public final class Diff<Value>
{
    private final Value emptyValue;
    private final List<Value> a;
    private final List<Value> b;
    private final BiPredicate<Value, Value> isEqual = Object::equals;

    private Diff(final Value emptyValue, final List<Value> a, final List<Value> b)
    {
        if (a.stream().anyMatch(value -> isEqual.test(value, emptyValue)) || b.stream().anyMatch(value -> isEqual.test(value, emptyValue)))
        {
            throw new IllegalArgumentException("Empty value " + emptyValue + " not allowed in the sequences");
        }
        this.emptyValue = emptyValue;
        this.a = copyOf(a);
        this.b = copyOf(b);
    }

    public static Diff<String> diff(final List<String> a, final List<String> b)
    {
        return diff("_", a, b);
    }

    public static <Value> Diff<Value> diff(final Value emptyValue, final List<Value> a, final List<Value> b)
    {
        return new Diff<>(emptyValue, a, b);
    }

    public Result<Value> result()
    {
        if (equal(isEqual, a, b))
        {
            return new Result<>(0, a, b);
        }
        if (a.isEmpty() || b.isEmpty())
        {
            final int expectedLength = Math.abs(a.size() - b.size());
            return new Result<>(expectedLength, appended(a, emptyValue, expectedLength), appended(b, emptyValue, expectedLength));
        }

        final Result<Value> result1 = diff(emptyValue, a.subList(1, a.size()), b).result();
        final Result<Value> result2 = diff(emptyValue, a, b.subList(1, b.size())).result();
        if (isEqual.test(a.get(0), b.get(0)))
        {
            final Result<Value> result3 = diff(emptyValue, a.subList(1, a.size()), b.subList(1, b.size())).result();
            if (result3.differences() < result1.differences() + 1 && result3.differences() < result2.differences())
            {
                return new Result<>(result3.differences(), prepended(a.get(0), result3.a), prepended(b.get(0), result3.b));
            }
        }
        if (result1.differences() < result2.differences())
        {
            return new Result<>(result1.differences() + 1, prepended(a.get(0), result1.a), prepended(emptyValue, result1.b));
        }
        else
        {
            return new Result<>(result2.differences() + 1, prepended(emptyValue, result2.a), prepended(b.get(0), result2.b));
        }
    }

}
