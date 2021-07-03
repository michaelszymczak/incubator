package com.michaelszymczak.sample;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(new Diff(List.of(), List.of()).result()).isEqualTo(new Result(0));
    }

    private static class Diff
    {
        public Diff(final List<String> a, final List<String> b)
        {

        }

        public Result result()
        {
            return new Result(0);
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
        public String toString()
        {
            return "Result{" +
                   "differences=" + differences +
                   '}';
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
        public int hashCode()
        {
            return differences;
        }
    }

}
