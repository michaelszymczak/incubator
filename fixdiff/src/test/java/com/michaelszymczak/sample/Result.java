package com.michaelszymczak.sample;

import java.util.List;
import java.util.stream.Collectors;

class Result<Value>
{
    final List<String> aLabels;
    final List<String> bLabels;
    final List<Value> aValues;
    final List<Value> bValues;
    private final int differences;

    public Result(final int differences, final List<Value> aValues, final List<Value> bValues)
    {
        this(differences, aValues.stream().map(Object::toString).collect(Collectors.toList()), bValues.stream().map(Object::toString).collect(Collectors.toList()), aValues, bValues);
    }

    public Result(final int differences, final List<String> aLabels, final List<String> bLabels, final List<Value> aValues, final List<Value> bValues)
    {
        if (aLabels.size() != bLabels.size() || aLabels.size() != aValues.size() || aLabels.size() != bValues.size())
        {
            throw new IllegalArgumentException("Wrong number of entries");
        }
        this.differences = differences;
        this.aLabels = aLabels;
        this.bLabels = bLabels;
        this.aValues = aValues;
        this.bValues = bValues;
    }

    public int differences()
    {
        return differences;
    }

    public int entriesCount()
    {
        return aLabels.size();
    }

    @Override
    public int hashCode()
    {
        int result = aValues != null ? aValues.hashCode() : 0;
        result = 31 * result + (bValues != null ? bValues.hashCode() : 0);
        result = 31 * result + (aLabels != null ? aLabels.hashCode() : 0);
        result = 31 * result + (bLabels != null ? bLabels.hashCode() : 0);
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
        if (aValues != null ? !aValues.equals(result.aValues) : result.aValues != null)
        {
            return false;
        }
        if (bValues != null ? !bValues.equals(result.bValues) : result.bValues != null)
        {
            return false;
        }
        if (aLabels != null ? !aLabels.equals(result.aLabels) : result.aLabels != null)
        {
            return false;
        }
        return bLabels != null ? bLabels.equals(result.bLabels) : result.bLabels == null;
    }

    @Override
    public String toString()
    {
        return "Result{" +
               "aLabels=" + aLabels +
               ", bLabels=" + bLabels +
               ", aValues=" + aValues +
               ", bValues=" + bValues +
               ", differences=" + differences +
               '}';
    }
}
