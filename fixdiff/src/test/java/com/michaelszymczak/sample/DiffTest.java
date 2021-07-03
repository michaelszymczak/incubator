package com.michaelszymczak.sample;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


import static java.util.List.of;

public class DiffTest
{
    @Test
    void emptySequencesShouldBeTheSame()
    {
        assertThat(new Diff(of(), of()).result()).isEqualTo(new Result(0, List.of(), List.of()));
    }

    @Test
    void shouldDetectTheSameSequences()
    {
        assertThat(new Diff(of("foo", "bar", "c"), of("foo", "bar", "c")).result()).isEqualTo(
                new Result(0, of("foo", "bar", "c"), of("foo", "bar", "c"))
        );
    }

    @Test
    void shouldUseTheLengthAsTheDifferenceBetweenEmptyAndNonEmptySequence()
    {
        assertThat(new Diff(of("foo", "bar", "c"), of()).result()).isEqualTo(
                new Result(3, of("foo", "bar", "c"), of("", "", ""))
        );
        assertThat(new Diff(of(), of("foo", "bar")).result()).isEqualTo(
                new Result(2, of("", ""), of("foo", "bar"))
        );
    }
}
