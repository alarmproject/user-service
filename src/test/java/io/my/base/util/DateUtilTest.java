package io.my.base.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class DateUtilTest {
    DateUtil dateUtil = new DateUtil();

    @Test
    void localDateTimeToUnixTime() {
        Long firstUnixTime = this.dateUtil.localDateTimeToUnixTime(LocalDateTime.now());
        Long secondUnixTime = this.dateUtil.localDateTimeToUnixTime(LocalDateTime.now());

        log.info("firstUnixTime: {}", firstUnixTime);
        log.info("secondUnixTime: {}", secondUnixTime);

        assertTrue(firstUnixTime <= secondUnixTime);
    }

    @Test
    void unixTimeToLocalDateTime() {
        LocalDateTime firstUnixTime = this.dateUtil.unixTimeToLocalDateTime(1647423158970L);
        LocalDateTime secondUnixTime = this.dateUtil.unixTimeToLocalDateTime(1647423158972L);

        log.info("firstUnixTime: {}", firstUnixTime);
        log.info("secondUnixTime: {}", secondUnixTime);

        assertTrue(firstUnixTime.isBefore(secondUnixTime));
    }

}