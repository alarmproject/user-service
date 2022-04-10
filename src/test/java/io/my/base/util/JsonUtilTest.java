package io.my.base.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class JsonUtilTest extends Mockito {
    private final JsonUtil jsonUtil;

    public JsonUtilTest() {
        this.jsonUtil = new JsonUtil();
    }

    @Test
    void objectToJson() throws JsonProcessingException {
        Map<String, Integer> map = new HashMap<>();
        map.put("id", 0);

        String actual = jsonUtil.objectToJson(map);

        ObjectMapper objectMapper = new ObjectMapper();
        String expect = objectMapper.writeValueAsString(map);

        assertEquals(expect, actual);
    }

    @Test
    void objectToByteArray() throws JsonProcessingException {
        Map<String, Integer> map = new HashMap<>();
        map.put("id", 0);

        byte[] actual = jsonUtil.objectToByteArray(map);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] expect = objectMapper.writeValueAsString(map).getBytes(StandardCharsets.UTF_8);

        assertSame(actual.length, expect.length);
        for (int index=0; index < actual.length; index++) {
            assertSame(actual[index], expect[index]);
        }
    }

}
