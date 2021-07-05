package com.michaelszymczak.sample.fixdiff.usecase;

import java.util.List;

import com.michaelszymczak.sample.fixdiff.Diff;
import com.michaelszymczak.sample.fixdiff.Summary;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


import static com.michaelszymczak.sample.fixdiff.Diff.diff;

public class DiffUseCaseTest
{
    @Test
    void shouldProvideMeaningfulDiff()
    {
        final Diff<Message> diff = diff(
                "_", new Message("----", "-----"), message -> message.type,
                List.of(new Message("X", "some\n content x"), new Message("Y", "some\n content y"), new Message("Z", "some content z")),
                List.of(new Message("X", "different\n content \nx"), new Message("Z", "some content z"))
        );

        final Summary summary = new Summary(diff.result());

        assertThat(summary.numberOfCompletelyDifferentItems()).isEqualTo(1);
        assertThat(summary.labelsA()).isEqualTo(
                "X\n" +
                "Y\n" +
                "Z"
        );
        assertThat(summary.labelsB()).isEqualTo(
                "X\n" +
                "_\n" +
                "Z"
        );
        assertThat(summary.entries()).hasSize(3);
        assertThat(summary.entries().get(0).a).isEqualTo(
                "type:X\n" +
                "some\n" +
                " content x"
        );
        assertThat(summary.entries().get(0).b).isEqualTo(
                "type:X\n" +
                "different\n" +
                " content \n" +
                "x"
        );
        assertThat(summary.entries().get(1).a).isEqualTo(
                "type:Y\n" +
                "some\n" +
                " content y"
        );
        assertThat(summary.entries().get(1).b).isEqualTo(
                "type:----\n" +
                "-----"
        );
        assertThat(summary.entries().get(2).a).isEqualTo(
                "type:Z\n" +
                "some content z"
        );
        assertThat(summary.entries().get(2).b).isEqualTo(
                "type:Z\n" +
                "some content z"
        );
    }

    private static class Message
    {
        final String type;
        final String body;

        public Message(final String type, final String body)
        {
            this.type = type;
            this.body = body;
        }

        @Override
        public String toString()
        {
            return "type:" + type + "\n" + body;
        }
    }
}
