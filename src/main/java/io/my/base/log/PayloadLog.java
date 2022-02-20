package io.my.base.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayloadLog {

    public void info(String message) {
        log.info(message);
    }

    public void info(String message, Object... objects) {
        log.info(message, objects);
    }

}
