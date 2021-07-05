package com.michaelszymczak.sample;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import static com.michaelszymczak.sample.Diff.diff;
import static java.util.List.of;

public class DiffTest
{
    @Test
    void emptySequencesShouldBeTheSame()
    {
        assertThat(diff("_", of(), of()).result()).isEqualTo(new Result<>(0, List.of(), List.of()));
    }

    @Test
    void shouldDetectTheSameSequences()
    {
        assertThat(diff("_", of("foo", "bar", "c"), of("foo", "bar", "c")).result()).isEqualTo(
                new Result<>(0, of("foo", "bar", "c"), of("foo", "bar", "c"))
        );
    }

    @Test
    void shouldUseTheLengthAsTheDifferenceBetweenEmptyAndNonEmptySequence()
    {
        assertThat(diff("_", of("foo", "bar", "c"), of()).result()).isEqualTo(
                new Result<>(3, of("foo", "bar", "c"), of("_", "_", "_"))
        );
        assertThat(diff("_", of(), of("foo", "bar")).result()).isEqualTo(
                new Result<>(2, of("_", "_"), of("foo", "bar"))
        );
    }

    @Test
    void shouldInsertItemMissingFromTheBeginningOfTheFirstSequence()
    {
        assertThat(diff("_", of("b", "c"), of("a", "b", "c")).result()).isEqualTo(
                new Result<>(1, of("_", "b", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertItemMissingFromTheBeginningOfTheSecondSequence()
    {
        assertThat(diff("_", of("a", "b", "c"), of("b", "c")).result()).isEqualTo(
                new Result<>(1, of("a", "b", "c"), of("_", "b", "c"))
        );
    }

    @Test
    void shouldInsertMultipleItemsMissingFromTheBeginningTheFirstSequence()
    {
        assertThat(diff("_", of("c"), of("a", "b", "c")).result()).isEqualTo(
                new Result<>(2, of("_", "_", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertMultipleItemsMissingFromTheBeginningTheSecondSequence()
    {
        assertThat(diff("_", of("a", "b", "c"), of("c")).result()).isEqualTo(
                new Result<>(2, of("a", "b", "c"), of("_", "_", "c"))
        );
    }

    @Test
    void shouldRearrangeTheItemsIntoMissingAndMatching()
    {
        assertThat(diff("_", of("a"), of("b")).result()).isEqualTo(
                new Result<>(2, of("_", "a"), of("b", "_"))
        );
    }

    @Test
    void shouldInsertTheMiddleItemMissingFromTheFirstSequence()
    {
        assertThat(diff("_", of("a", "c"), of("a", "b", "c")).result()).isEqualTo(
                new Result<>(1, of("a", "_", "c"), of("a", "b", "c"))
        );
    }

    @Test
    void shouldInsertTheMiddleItemMissingFromTheSecondSequence()
    {
        assertThat(diff("_", of("a", "b", "c"), of("a", "c")).result()).isEqualTo(
                new Result<>(1, of("a", "b", "c"), of("a", "_", "c"))
        );
    }

    @Test
    void shouldRearrangeMultipleItemsIntoMissingAndMatching()
    {
        assertThat(diff("_", of("a", "b", "c", "d"), of("b", "c", "e")).result()).isEqualTo(
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
        assertThat(diff("_", of("a", "b"), of("ab")).result()).isEqualTo(
                new Result<>(3, of("_", "a", "b"), of("ab", "_", "_"))
        );
    }

    @Test
    void shouldFindMinimumDiffForLongerSequence()
    {
        assertThat(diff(
                "_", of("a", "b", "c", "d", "e", "f", "g", "h", "i", "l", "o", "p"),
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
        assertThat(diff("[EMPTY]", "[EMPTY]", Object::toString, List.of("a"), List.of()).result()).isEqualTo(
                new Result<>(1, List.of("a"), List.of("[EMPTY]")));
    }

    @Test
    void shouldAllowAnyValueType()
    {
        assertThat(diff("X", 0, Object::toString, of(1, 2, 3, 4), of(2, 3, 6)).result()).isEqualTo(
                new Result<>(
                        3,
                        of("1", "2", "3", "X", "4"),
                        of("X", "2", "3", "6", "X"),
                        of(1, 2, 3, 0, 4),
                        of(0, 2, 3, 6, 0)
                )
        );
    }

    @Test
    void shouldNotAllowEmptyValueInTheSequence()
    {
        assertThatThrownBy(() -> diff("x", "x", Object::toString, List.of("a", "x"), List.of())).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> diff("x", "x", Object::toString, List.of("a"), List.of("a", "x", "b"))).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> diff("5", 5, Object::toString, List.of(5), List.of())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAllowCustomEqualityLogic()
    {
        assertThat(diff("", "_", value -> value.substring(0, 1),
                        of("a1"), of()
        ).result()).isEqualTo(
                new Result<>(
                        1,
                        of("a"),
                        of(""),
                        of("a1"),
                        of("_")
                )
        );

        assertThat(diff("", "", value -> value.substring(0, 1),
                        of("a1", "b1"), of("a2", "b2")
        ).result()).isEqualTo(
                new Result<>(
                        0,
                        of("a", "b"),
                        of("a", "b"),
                        of("a1", "b1"),
                        of("a2", "b2")
                )
        );

        assertThat(diff("_", "", value -> value.substring(0, 1),
                        of("b1", "c1"), of("a2", "b2", "c2")
        ).result()).isEqualTo(
                new Result<>(
                        1,
                        of("_", "b", "c"),
                        of("a", "b", "c"),
                        of("", "b1", "c1"),
                        of("a2", "b2", "c2")
                )
        );

        assertThat(diff("__", "__", value -> value.substring(0, 1),
                        of("a2", "b2", "c2"), of("b1", "c1")
        ).result()).isEqualTo(
                new Result<>(
                        1,
                        of("a", "b", "c"),
                        of("__", "b", "c"),
                        of("a2", "b2", "c2"),
                        of("__", "b1", "c1")
                )
        );

        assertThat(diff("__", "__", value -> value.substring(0, 1),
                        of("a1", "b1", "c1", "d1"), of("a1", "c2", "e1")
        ).result()).isEqualTo(
                new Result<>(
                        3,
                        of("a", "b", "c", "__", "d"),
                        of("a", "__", "c", "e", "__"),
                        of("a1", "b1", "c1", "__", "d1"),
                        of("a1", "__", "c2", "e1", "__")
                )
        );

        assertThat(diff(
                "_", "_", String::toLowerCase,
                of("a", "b", "c", "d", "e", "f", "g", "h", "i", "l", "o", "p"),
                of("A", "B", "C", "D", "E", "F", "G", "H", "I", "L", "O", "P")
        ).result()).isEqualTo(
                new Result<>(
                        0,
                        of("a", "b", "c", "d", "e", "f", "g", "h", "i", "l", "o", "p"),
                        of("a", "b", "c", "d", "e", "f", "g", "h", "i", "l", "o", "p"),
                        of("a", "b", "c", "d", "e", "f", "g", "h", "i", "l", "o", "p"),
                        of("A", "B", "C", "D", "E", "F", "G", "H", "I", "L", "O", "P")
                )
        );
    }
}
