package com.michaelszymczak.sample;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


import static java.util.List.copyOf;
import static java.util.List.of;

public class DiffTest
{
    @Test
    void shouldBeAbleToCompareResults()
    {
        assertThat(new Result(3)).isEqualTo(new Result(3));
        assertThat(new Result(4)).isNotEqualTo(new Result(3));
    }

    @Test
    void emptySequencesShouldBeTheSame()
    {
        assertThat(new Diff(of(), of()).result()).isEqualTo(new Result(0));
    }

    @Test
    void shouldUseTheLengthAsTheDifferenceBetweenEmptyAndNonEmptySequence()
    {
        assertThat(new Diff(of("foo", "bar", "c"), of()).result()).isEqualTo(new Result(3));
        assertThat(new Diff(of(), of("foo", "bar")).result()).isEqualTo(new Result(2));
    }

    private static class Diff
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

    private static class Result
    {
        final int differences;

        public Result(final int differences)
        {
            this.differences = differences;
        }

        @Override
        public int hashCode()
        {
            return differences;
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

            final Result result = (Result)o;

            return differences == result.differences;
        }

        @Override
        public String toString()
        {
            return "Result{" +
                   "differences=" + differences +
                   '}';
        }
    }

}
