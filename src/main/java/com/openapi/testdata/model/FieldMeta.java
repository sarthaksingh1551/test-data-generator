package com.openapi.testdata.model;

import java.util.List;

public record FieldMeta(
        String name,
        String type,
        boolean required,
        Integer minLength,
        Integer maxLength,
        Integer minimum,
        Integer maximum,
        List<String> enumValues,
        String format,
        boolean isArray,
        String arrayItemType,
        Integer minItems,
        Integer maxItems
) {
}