package io.my.slack;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackService {
    private final SlackProperties slackProperties;

    public void sendSlackException(Exception e) {
        var client = Slack.getInstance().methods(slackProperties.getBotUserOauthToken());

        try {
            StringWriter printStacktrace = new StringWriter();
            e.printStackTrace(new PrintWriter(printStacktrace));

            String backendExceptionChannelId = "C03GZHAN6TZ";
            var response = client.chatPostMessage(ChatPostMessageRequest.builder()
                    .token(slackProperties.getBotUserOauthToken())
                    .channel(backendExceptionChannelId)
                    .text(printStacktrace.toString())
                    .build());
            log.info("isOk: {}", response.isOk());
        } catch (IOException | SlackApiException exception) { exception.printStackTrace(); }
    }
}
