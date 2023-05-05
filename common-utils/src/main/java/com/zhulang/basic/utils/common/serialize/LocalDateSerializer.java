package com.zhulang.basic.utils.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 * @Author zhulang
 * @Date 2023-05-05
 **/
public class LocalDateSerializer extends JsonSerializer<LocalDate> {
    public LocalDateSerializer() {
    }

    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(DateTimeFormatter.ISO_LOCAL_DATE.format(date));
    }
}

