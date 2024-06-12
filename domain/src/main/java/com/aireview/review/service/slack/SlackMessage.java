package com.aireview.review.service.slack;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SlackMessage {
    private String text;

    public SlackMessage(String text) {
        this.text = text;
    }
}
