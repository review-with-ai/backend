package com.aireview.review.service.slack;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "slackNotificationFeign", url = "${slack.url}")
public interface SlackNotificationFeign {
    @PostMapping
    void sendMessage(@RequestBody SlackMessage slackMessage);
}
