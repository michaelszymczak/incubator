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
        assertThat(new Result(0, List.of(), List.of())).isEqualTo(new Result(0, List.of(), List.of()));
        assertThat(new Result(1, List.of(""), List.of("a"))).isEqualTo(new Result(1, List.of(""), List.of("a")));
        assertThat(new Result(1, List.of(""), List.of("a"))).isNotEqualTo(new Result(0, List.of("a"), List.of("a")));
        assertThat(new Result(1, List.of(""), List.of("a"))).isNotEqualTo(new Result(1, List.of("a"), List.of("")));
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
        private final List<String> a;
        private final List<String> b;

        public Result(final int differences)
        {
            this(differences, List.of(), List.of());
        }

        public Result(final int differences, final List<String> a, final List<String> b)
        {
            this.differences = differences;
            this.a = a;
            this.b = b;
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
        public int hashCode()
        {
            int result = differences;
            result = 31 * result + (a != null ? a.hashCode() : 0);
            result = 31 * result + (b != null ? b.hashCode() : 0);
            return result;
        }

        @Override
        public String toString()
        {
            return "Result{" +
                   "differences=" + differences +
                   ", a=" + a +
                   ", b=" + b +
                   '}';
        }
    }

}
