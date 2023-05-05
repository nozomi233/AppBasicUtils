package com.zhulang.basic.utils.common.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    public LocalDateTimeDeserializer() {
    }

    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateTimeStr = ((JsonNode)parser.getCodec().readTree(parser)).asText();
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}