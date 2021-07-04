package com.michaelszymczak.sample;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import static java.util.List.of;

public class DiffTest
{
    @Test
    void emptySequencesShouldBeTheSame()
    {
        assertThat(Diff.diff(of(), of()).result()).isEqualTo(new Result<>(0, List.of(), List.of()));
    }

    @Test
    void shouldDetectTheSameSequences()
    {
        assertThat(Diff.diff(of("foo", "bar", "c"), of("foo", "bar", "c")).result()).isEqualTo(
                new Result<>(0, of("foo", "bar", "c"), of("foo", "bar", "c"))
        );
    }

    @Test
    void shouldUseTheLengthAsTheDifferenceBetweenEmptyAndNonEmptySequence()
    {
        assertThat(Diff.diff(of("foo", "bar", "c"), of()).result()).isEqualTo(
                new Result<>(3, of("foo", "bar", "c"), of("_", "_", "_"))
        );
        assertThat(Diff.diff(of(), of("foo", "bar")).result()).isEqualTo(
                new Result<>(2, of("_", "_"), of("foo", "bar"))
        );
    }

    @Test
    void shouldInsertItemMissingFromTheBeginningOfTheFirstSequence()
    {
        assertThat(Diff.diff(of("b", "c"), of("a", "b", "c")).result()).isEqualTo(
                new Result<>(1, of("_", "b", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertItemMissingFromTheBeginningOfTheSecondSequence()
    {
        assertThat(Diff.diff(of("a", "b", "c"), of("b", "c")).result()).isEqualTo(
                new Result<>(1, of("a", "b", "c"), of("_", "b", "c"))
        );
    }

    @Test
    void shouldInsertMultipleItemsMissingFromTheBeginningTheFirstSequence()
    {
        assertThat(Diff.diff(of("c"), of("a", "b", "c")).result()).isEqualTo(
                new Result<>(2, of("_", "_", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertMultipleItemsMissingFromTheBeginningTheSecondSequence()
    {
        assertThat(Diff.diff(of("a", "b", "c"), of("c")).result()).isEqualTo(
                new Result<>(2, of("a", "b", "c"), of("_", "_", "c"))
        );
    }

    @Test
    void shouldRearrangeTheItemsIntoMissingAndMatching()
    {
        assertThat(Diff.diff(of("a"), of("b")).result()).isEqualTo(
                new Result<>(2, of("_", "a"), of("b", "_"))
        );
    }

    @Test
    void shouldInsertTheMiddleItemMissingFromTheFirstSequence()
    {
        assertThat(Diff.diff(of("a", "c"), of("a", "b", "c")).result()).isEqualTo(
                new Result<>(1, of("a", "_", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertTheMiddleItemMissingFromTheSecondSequence()
    {
        assertThat(Diff.diff(of("a", "b", "c"), of("a", "c")).result()).isEqualTo(
                new Result<>(1, of("a", "b", "c"), of("a", "_", "c"))
        );
    }

    @Test
    void shouldRearrangeMultipleItemsIntoMissingAndMatching()
    {
        assertThat(Diff.diff(of("a", "b", "c", "d"), of("b", "c", "e")).result()).isEqualTo(
                new Result<>(
                        3,
                        of("a", "b", "c", "_", "d"),
                        of("_", "b", "c", "e", "_")
                )
        );
    }

    @Test
    void shouldNotMergeMultipleItemsEvenIfTheyLookTheSame()
    {
        assertThat(Diff.diff(of("a", "b"), of("ab")).result()).isEqualTo(
                new Result<>(3, of("_", "a", "b"), of("ab", "_", "_"))
        );
    }

    @Test
    void shouldFindMinimumDiffForLongerSequence()
    {
        assertThat(Diff.diff(
                of("a", "b", "c", "d", "e", "f", "g", "h", "i", "l", "o", "p"),
                of("b", "d", "e", "f", "k", "l", "m", "n", "o", "p")
        ).result()).isEqualTo(
                new Result<>(
                        8,
                        of("a", "b", "c", "d", "e", "f", "_", "g", "h", "i", "l", "_", "_", "o", "p"),
                        of("_", "b", "_", "d", "e", "f", "k", "_", "_", "_", "l", "m", "n", "o", "p")
                )
        );
    }

    @Test
    void shouldMakeEmptyValueCustomizable()
    {
        assertThat(new Diff<>("[EMPTY]", List.of("a"), List.of()).result()).isEqualTo(
                new Result<>(1, List.of("a"), List.of("[EMPTY]")));
    }

    @Test
    void shouldAllowAnyValueType()
    {
        assertThat(new Diff<>(0, of(1, 2, 3, 4), of(2, 3, 6)).result()).isEqualTo(
                new Result<>(
                        3,
                        of(1, 2, 3, 0, 4),
                        of(0, 2, 3, 6, 0)
                )
        );
    }

    @Test
    void shouldNotAllowEmptyValueInTheSequence()
    {
        assertThatThrownBy(() -> new Diff<>("x", List.of("a", "x"), List.of())).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Diff<>("x", List.of("a"), List.of("a", "x", "b"))).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Diff<>(5, List.of(5), List.of())).isInstanceOf(IllegalArgumentException.class);
    }
}
