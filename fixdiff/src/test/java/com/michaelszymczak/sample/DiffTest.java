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

    @Test
    void shouldInsertItemMissingFromTheBeginningOfTheFirstSequence()
    {
        assertThat(new Diff(of("b", "c"), of("a", "b", "c")).result()).isEqualTo(
                new Result(1, of("", "b", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertItemMissingFromTheBeginningOfTheSecondSequence()
    {
        assertThat(new Diff(of("a", "b", "c"), of("b", "c")).result()).isEqualTo(
                new Result(1, of("a", "b", "c"), of("", "b", "c"))
        );
    }

    @Test
    void shouldInsertMultipleItemsMissingFromTheBeginningTheFirstSequence()
    {
        assertThat(new Diff(of("c"), of("a", "b", "c")).result()).isEqualTo(
                new Result(2, of("", "", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertMultipleItemsMissingFromTheBeginningTheSecondSequence()
    {
        assertThat(new Diff(of("a", "b", "c"), of("c")).result()).isEqualTo(
                new Result(2, of("a", "b", "c"), of("", "", "c"))
        );
    }

    @Test
    void shouldRearrangeTheItemsIntoMissingAndMatching()
    {
        assertThat(new Diff(of("a"), of("b")).result()).isEqualTo(
                new Result(2, of("", "a"), of("b", ""))
        );
    }

    @Test
    void shouldInsertTheMiddleItemMissingFromTheFirstSequence()
    {
        assertThat(new Diff(of("a", "c"), of("a", "b", "c")).result()).isEqualTo(
                new Result(1, of("a", "", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertTheMiddleItemMissingFromTheSecondSequence()
    {
        assertThat(new Diff(of("a", "b", "c"), of("a", "c")).result()).isEqualTo(
                new Result(1, of("a", "b", "c"), of("a", "", "c"))
        );
    }

    @Test
    void shouldRearrangeMultipleItemsIntoMissingAndMatching()
    {
        assertThat(new Diff(of("a", "b", "c", "d"), of("b", "c", "e")).result()).isEqualTo(
                new Result(
                        3,
                        of("a", "b", "c", "", "d"),
                        of("", "b", "c", "e", "")
                )
        );
    }
}
