package com.michaelszymczak.sample;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest
{
    @Test
    void shouldBeTheSameResultWhenTheSameDifferences()
    {
        assertThat(new Result<>(0, List.of(), List.of())).isEqualTo(new Result<>(0, List.of(), List.of()));
        assertThat(new Result<>(1, List.of(""), List.of("a"))).isEqualTo(new Result<>(1, List.of(""), List.of("a")));
    }

    @Test
    void shouldNotBeTheSameResultWhenDifferentDifferences()
    {
        assertThat(new Result<>(1, List.of(""), List.of("a"))).isNotEqualTo(new Result<>(0, List.of("a"), List.of("a")));
        assertThat(new Result<>(1, List.of(""), List.of("a"))).isNotEqualTo(new Result<>(1, List.of("a"), List.of("")));
    }

    @Test
    void shouldNotBeTheSameWhenDifferentLabels()
    {
        assertThat(new Result<>(0, List.of("a"), List.of("b"), List.of("a sth"), List.of("b sth")))
                .isNotEqualTo(new Result<>(0, List.of("A"), List.of("b"), List.of("a sth"), List.of("b sth")));
        assertThat(new Result<>(0, List.of("a"), List.of("b"), List.of("a sth"), List.of("b sth")))
                .isNotEqualTo(new Result<>(0, List.of("a"), List.of("B"), List.of("a sth"), List.of("b sth")));
    }
}