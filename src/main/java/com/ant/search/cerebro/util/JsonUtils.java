package com.ant.search.cerebro.util;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.flattener.FlattenMode;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@Slf4j
public class JsonUtils {
    public static Optional<String> stringifyJson(final Map<String, Object> json, final ObjectMapper objectMapper) {
        try {
            return Optional.of(objectMapper.writeValueAsString(json));
        } catch (final Exception ex) {
            log.error("Error converting JSON to string with error {}", ex.getMessage());
        }
        return Optional.empty();
    }

    public static Optional<Map<String, Object>> parseStringToJson(final String json, final ObjectMapper objectMapper) {
        try {
            return Optional.of(objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {}));
        } catch (final Exception ex) {
            log.error("Error converting String to JSON with error {}", ex.getMessage());
        }
        return Optional.empty();
    }

    public static Map<String, Object> flattenJson(final Map<String, Object> json, final ObjectMapper objectMapper) {
        final Map<String, Object> flattenedDocument = new JsonFlattener(JsonUtils.stringifyJson(json, objectMapper).orElse(""))
                .withFlattenMode(FlattenMode.KEEP_PRIMITIVE_ARRAYS).flattenAsMap();
        return parseStringToJson(stringifyJson(flattenedDocument, objectMapper).orElse(""), objectMapper).orElse(Collections.emptyMap());
    }

    public static Map<String, Object> unFlattenJson(final Map<String, Object> json, final ObjectMapper objectMapper) {
        return JsonUtils.parseStringToJson(JsonUnflattener.unflatten(JsonUtils.stringifyJson(json, objectMapper).orElse("")), objectMapper)
                        .orElse(Collections.emptyMap());
    }
}
