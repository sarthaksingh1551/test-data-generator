package com.openapi.testdata.generator;

import com.openapi.testdata.model.FieldMeta;
import com.openapi.testdata.model.SchemaMeta;

import java.util.*;

public class PayloadGenerator {

    public Map<String, Object> generatePositive(SchemaMeta schema) {
        Map<String, Object> payload = new HashMap<>();

        for (FieldMeta field : schema.fields().values()) {
            Object value = generateValidValue(field);
            payload.put(field.name(), value);
        }

        return payload;
    }

    public List<Map<String, Object>> generateEdgeCases(SchemaMeta schema) {
        List<Map<String, Object>> edgeCases = new ArrayList<>();

        for (FieldMeta field : schema.fields().values()) {

            List<Object> edgeValues = generateEdgeValues(field);

            for (Object edgeValue : edgeValues) {
                Map<String, Object> payload = generatePositive(schema);
                payload.put(field.name(), edgeValue);
                edgeCases.add(payload);
            }
        }

        return edgeCases;
    }

    public List<Map<String, Object>> generateNegativeCases(SchemaMeta schema) {
        List<Map<String, Object>> negativeCases = new ArrayList<>();

        for (FieldMeta field : schema.fields().values()) {

            List<Object> negativeValues = generateNegativeValues(field);

            for (Object negativeValue : negativeValues) {
                Map<String, Object> payload = generatePositive(schema);
                payload.put(field.name(), negativeValue);
                negativeCases.add(payload);
            }

            if (field.required()) {
                Map<String, Object> missingFieldPayload = generatePositive(schema);
                missingFieldPayload.remove(field.name());
                negativeCases.add(missingFieldPayload);
            }
        }

        return negativeCases;
    }

    // -------------------- VALUE GENERATION --------------------

    private Object generateValidValue(FieldMeta field) {

        if (field.enumValues() != null && !field.enumValues().isEmpty()) {
            return field.enumValues().get(0);
        }

        return switch (field.type()) {
            case "string" -> generateValidString(field);
            case "integer" -> generateValidInteger(field);
            case "boolean" -> true;
            case "array" -> generateValidArray(field);
            default -> null;
        };
    }

    private List<Object> generateValidArray(FieldMeta field) {

        int size = field.minItems() != null ? field.minItems() : 1;

        List<Object> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(generateValidArrayItem(field));
        }

        return list;
    }

    private Object generateValidString(FieldMeta field) {

        if ("email".equals(field.format())) {
            return "test@example.com";
        }

        int length = field.minLength() != null ? field.minLength() : 5;
        return "a".repeat(length);
    }

    private Object generateValidInteger(FieldMeta field) {

        if (field.minimum() != null) {
            return field.minimum();
        }

        return 1;
    }

    private Object generateValidArrayItem(FieldMeta field) {

        if ("string".equals(field.arrayItemType())) {
            return "item";
        }

        if ("integer".equals(field.arrayItemType())) {
            return 1;
        }

        return null;
    }

    private List<Object> generateEdgeValues(FieldMeta field) {

        List<Object> edges = new ArrayList<>();

        if ("string".equals(field.type())) {
            if (field.minLength() != null)
                edges.add("a".repeat(field.minLength()));

            if (field.maxLength() != null)
                edges.add("a".repeat(field.maxLength()));
        }

        if ("integer".equals(field.type())) {
            if (field.minimum() != null)
                edges.add(field.minimum());

            if (field.maximum() != null)
                edges.add(field.maximum());
        }

        return edges;
    }

    private List<Object> generateNegativeValues(FieldMeta field) {

        List<Object> negatives = new ArrayList<>();

        // Wrong type
        if ("string".equals(field.type())) {
            negatives.add(123);
        } else if ("integer".equals(field.type())) {
            negatives.add("wrongType");
        }

        // Enum violation
        if (field.enumValues() != null) {
            negatives.add("INVALID_ENUM");
        }

        // Below minimum
        if (field.minimum() != null) {
            negatives.add(field.minimum() - 1);
        }

        // Above maximum
        if (field.maximum() != null) {
            negatives.add(field.maximum() + 1);
        }

        return negatives;
    }
}