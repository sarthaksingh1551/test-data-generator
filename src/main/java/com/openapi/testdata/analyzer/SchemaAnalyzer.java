package com.openapi.testdata.analyzer;

import com.openapi.testdata.model.FieldMeta;
import com.openapi.testdata.model.SchemaMeta;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

import java.util.*;

public class SchemaAnalyzer
{
    public List<SchemaMeta> analyze(OpenAPI openAPI) {

        List<SchemaMeta> result = new ArrayList<>();

        if (openAPI.getComponents() == null ||
                openAPI.getComponents().getSchemas() == null) {
            return result;
        }

        Map<String, Schema> schemas = openAPI.getComponents().getSchemas();

        for (Map.Entry<String, Schema> entry : schemas.entrySet()) {

            String schemaName = entry.getKey();
            Schema schema = entry.getValue();

            Map<String, FieldMeta> fieldMap = new HashMap<>();

            List<String> requiredFields = schema.getRequired() != null
                    ? schema.getRequired()
                    : Collections.emptyList();

            if (schema.getProperties() != null) {

                Map<String, Schema> properties = schema.getProperties();

                for (Map.Entry<String, Schema> propertyEntry : properties.entrySet()) {

                    String fieldName = propertyEntry.getKey();
                    Schema fieldSchema = propertyEntry.getValue();

                    boolean required = requiredFields.contains(fieldName);

                    String type = fieldSchema.getType();
                    String format = fieldSchema.getFormat();

                    Integer minLength = fieldSchema.getMinLength();
                    Integer maxLength = fieldSchema.getMaxLength();

                    Integer minimum = fieldSchema.getMinimum() != null
                            ? fieldSchema.getMinimum().intValue()
                            : null;

                    Integer maximum = fieldSchema.getMaximum() != null
                            ? fieldSchema.getMaximum().intValue()
                            : null;

                    List<String> enumValues = fieldSchema.getEnum() != null
                            ? (List<String>) fieldSchema.getEnum()
                            : null;

                    boolean isArray = "array".equals(type);
                    String arrayItemType = null;
                    Integer minItems = null;
                    Integer maxItems = null;

                    if (isArray) {

                        if (fieldSchema.getItems() != null) {
                            arrayItemType = fieldSchema.getItems().getType();
                        }

                        minItems = fieldSchema.getMinItems();
                        maxItems = fieldSchema.getMaxItems();
                    }

                    FieldMeta fieldMeta = new FieldMeta(
                            fieldName,
                            type,
                            required,
                            minLength,
                            maxLength,
                            minimum,
                            maximum,
                            enumValues,
                            format,
                            isArray,
                            arrayItemType,
                            minItems,
                            maxItems
                    );

                    fieldMap.put(fieldName, fieldMeta);
                }
            }

            result.add(new SchemaMeta(schemaName, fieldMap));
        }

        return result;
    }
}
