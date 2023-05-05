package com.zhulang.basic.utils.common.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    public LocalDateDeserializer() {
    }

    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateString = ((JsonNode)parser.getCodec().readTree(parser)).asText();
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
