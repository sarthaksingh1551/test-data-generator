package com.openapi.testdata.model;

import java.util.Map;

public record SchemaMeta(
        String name,
        Map<String, FieldMeta> fields
) {
}