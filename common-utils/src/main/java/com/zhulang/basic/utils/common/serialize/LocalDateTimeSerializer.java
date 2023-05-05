package com.zhulang.basic.utils.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    public LocalDateTimeSerializer() {
    }

    public void serialize(LocalDateTime dateTime, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateTime));
    }
}
