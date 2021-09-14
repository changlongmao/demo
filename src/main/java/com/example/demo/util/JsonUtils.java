package com.example.demo.util;

import com.example.demo.exception.JsonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 *
 * <pre>
 * 支持JSON与对象间的互相转换
 * </pre>
 *
 * @author Deolin 2018-04-02
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper om = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        ObjectMapper om = new ObjectMapper();

        // 反序列化时，忽略Javabean中Collection属性对应JSON Array中的为null的元素
//        om.registerModule(new IgnoreCollectionNullElementDeserializeModule());

        // 反序列化时，忽略Javabean中不存在的属性，而不是抛出异常
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略入参没有任何属性导致的序列化报错
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return om;
    }

    private JsonUtils() {
        throw new UnsupportedOperationException("Never instantiate me.");
    }

    /**
     * 将对象转化为JSON
     */
    public static String toJson(Object object) {
        return toJson(object, om);
    }

    /**
     * 将对象转化为JSON
     */
    public static String toJson(Object object, ObjectMapper om) {
        try {
            return om.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("object={}", object, e);
            throw new JsonException(e);
        }
    }

    /**
     * 将对象转化为JSON，结果是美化的
     */
    public static String toJsonPrettily(Object object) {
        return toJsonPrettily(object, om);
    }

    /**
     * 将对象转化为JSON，结果是美化的
     */
    public static String toJsonPrettily(Object object, ObjectMapper om) {
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("object={}", object, e);
            throw new JsonException("转化JSON失败");
        }
    }

    /**
     * 压缩JSON（去除美化JSON中多余的换行与空格，如果参数字符串不是一个JSON，则无事发生）
     */
    public static String compressJson(String json) {
        try {
            Map<?, ?> map = om.readValue(json, Map.class);
            return toJson(map);
        } catch (Exception e) {
            // is not a json
            return json;
        }
    }

    /**
     * 将JSON转化为对象
     *
     * @throws JsonException 任何原因转化失败时，抛出这个异常，如果需要补偿处理，可以进行捕获
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return toObject(json, clazz, om);
    }

    /**
     * 将JSON转化为对象
     *
     * @throws JsonException 任何原因转化失败时，抛出这个异常，如果需要补偿处理，可以进行捕获
     */
    public static <T> T toObject(String json, Class<T> clazz, ObjectMapper om) {
        try {
            return om.readValue(json, clazz);
        } catch (IOException e) {
            log.error("json={}, clazz={}", json, clazz, e);
            throw new JsonException(e);
        }
    }

    /**
     * 将JSON转化为对象列表
     *
     * @throws JsonException 任何原因转化失败时，抛出这个异常，如果需要补偿处理，可以进行捕获
     */
    public static <T> List<T> toListOfObject(String json, Class<T> clazz) {
        return toListOfObject(json, clazz, om);
    }

    /**
     * 将JSON转化为对象列表
     *
     * @throws JsonException 任何原因转化失败时，抛出这个异常，如果需要补偿处理，可以进行捕获
     */

    public static <T> List<T> toListOfObject(String json, Class<T> clazz, ObjectMapper om) {
        try {
            @SuppressWarnings("unchecked") Class<T[]> arrayClass = (Class<T[]>) Class
                    .forName("[L" + clazz.getName() + ";");
            return Lists.newArrayList(om.readValue(json, arrayClass));
        } catch (IOException | ClassNotFoundException e) {
            log.error("json={}, clazz={}", json, clazz, e);
            throw new JsonException(e);
        }
    }

    /**
     * JSON -> 参数化的对象
     *
     * 示例： Collection<<User<UserAddress>> users = JsonUtils.toParameterizedObject(text);
     *
     * @throws JsonException 任何原因转化失败时，抛出这个异常，如果需要补偿处理，可以进行捕获
     */
    public static <T> T toParameterizedObject(String json, TypeReference<T> typeReference) {
        return toParameterizedObject(json, typeReference, om);
    }

    /**
     * JSON -> 参数化的对象
     *
     * 示例： Collection<<User<UserAddress>> users = JsonUtils.toParameterizedObject(text);
     *
     * @throws JsonException 任何原因转化失败时，抛出这个异常，如果需要补偿处理，可以进行捕获
     */
    public static <T> T toParameterizedObject(String json, TypeReference<T> typeReference, ObjectMapper om) {
        try {
            return om.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("json={}, typeReference={}", json, typeReference, e);
            throw new JsonException(e);
        }
    }

    /**
     * JSON -> JsonNode对象
     *
     * <strong>除非JSON对应数据结构在运行时是变化的，否则不建议使这个方法</strong>
     *
     * @throws JsonException 任何原因转化失败时，抛出这个异常，如果需要补偿处理，可以进行捕获
     */
    public static JsonNode toTree(String json) {
        return toTree(json, om);
    }

    /**
     * JSON -> JsonNode对象
     *
     * <strong>除非JSON对应数据结构在运行时是变化的，否则不建议使这个方法</strong>
     *
     * @throws JsonException 任何原因转化失败时，抛出这个异常，如果需要补偿处理，可以进行捕获
     */
    public static JsonNode toTree(String json, ObjectMapper om) {
        try {
            return om.readTree(json);
        } catch (IOException e) {
            log.error("json={}", json, e);
            throw new JsonException(e);
        }
    }

    /**
     * @deprecated 这个属性已被废弃，代替方案是<code>ObjectMapper yours = new ObjectMapper();</>
     */
    @Deprecated
    public static final ObjectMapper mapper = new ObjectMapper();

    /**
     * @deprecated 这个方法已被废弃，代替方案是{@link JsonUtils#toJson(Object)}
     */
    @Deprecated
    @Nullable
    public static String serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    /**
     * @deprecated 这个方法已被废弃，代替方案是{@link JsonUtils#toObject(String, Class)}
     */
    @Deprecated
    @Nullable
    public static <T> T parse(String json, Class<T> tClass) {
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * @deprecated 这个方法已被废弃，代替方案是{@link JsonUtils#toListOfObject(String, Class<T>)}
     */
    @Deprecated
    @Nullable
    public static <E> List<E> parseList(String json, Class<E> eClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * @deprecated 这个方法已被废弃，代替方案是{@link JsonUtils#toParameterizedObject(String,
     *         TypeReference)}
     */
    @Deprecated
    @Nullable
    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

    /**
     * @deprecated 这个方法已被废弃，代替方案是{@link JsonUtils#toParameterizedObject(String,
     *         TypeReference)}
     */
    @Deprecated
    @Nullable
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            log.error("json解析出错：" + json, e);
            return null;
        }
    }

}
