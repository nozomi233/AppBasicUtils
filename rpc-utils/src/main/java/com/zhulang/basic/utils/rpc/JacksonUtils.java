package com.zhulang.basic.utils.rpc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zhulang.basic.utils.common.serialize.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 仅用在内部，不参与参数的序列化/反序列化，参数、返回值请使用codec模块
 * @Author zhulang
 * @Date 2023-05-05
 */
@Slf4j
public class JacksonUtils {
    private static final ObjectMapper MAPPER;
    private static final Map<Type, JavaType> TYPE_JAVA_TYPE_MAP = new ConcurrentHashMap<>();

    static {
        MAPPER = (new ObjectMapper()).registerModule(initModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
    }

    private static SimpleModule initModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer())
                .addDeserializer(LocalDate.class, new LocalDateDeserializer());
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        return module;
    }

    public static String toJsonString(Object result) {
        try {
            return MAPPER.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.error("Fail to Serialize object!", e);
            return "";
        }
    }

    public static <T> T parseObject(byte[] result, Class<T> clazz) {
        try {
            return MAPPER.readValue(result, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse object!", e);
        }
    }

    public static <T> T parseObject(String result, Type type) {
        try {
            return MAPPER.readValue(result, JacksonUtils.getJavaType(type));
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse object!", e);
        }
    }

    public static <T> T parseObject(String result, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(result, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse object!", e);
        }
    }

    public static <T> T parseObject(String result, Class<T> clazz) {
        try {
            return MAPPER.readValue(result, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse object!", e);
        }
    }

    public static <T> T parseObject(String result, JavaType javaType) {
        try {
            return MAPPER.readValue(result, javaType);
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse object!", e);
        }
    }

    public static JavaType getJavaType(Class<?> clazz) {
        return getJavaType((Type) clazz);
    }

    public static JavaType getJavaType(Type type) {
        if (type instanceof JavaType) {
            return (JavaType) type;
        }
        return TYPE_JAVA_TYPE_MAP.computeIfAbsent(type, c -> MAPPER.getTypeFactory().constructType(c));
    }

    public static JavaType getJavaType(TypeReference<?> typeReference) {
        return getJavaType(typeReference.getType());
    }
}
