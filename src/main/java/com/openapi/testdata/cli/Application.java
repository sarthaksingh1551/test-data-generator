package com.openapi.testdata.cli;

import com.openapi.testdata.OpenApiParser;
import com.openapi.testdata.analyzer.SchemaAnalyzer;
import com.openapi.testdata.generator.PayloadGenerator;
import io.swagger.v3.oas.models.OpenAPI;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Application {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = Application.class
                .getClassLoader()
                .getResourceAsStream("spec/sample.json");

        if (inputStream == null) {
            System.out.println("Spec file not found inside JAR!");
            return;
        }

        String content = new String(
                inputStream.readAllBytes(),
                java.nio.charset.StandardCharsets.UTF_8
        );

        OpenApiParser parser = new OpenApiParser();
        OpenAPI openAPI = parser.parse(content);

        if (openAPI == null) {
            System.out.println("OpenAPI parsing returned null.");
            return;
        }

        SchemaAnalyzer analyzer = new SchemaAnalyzer();
        var schemas = analyzer.analyze(openAPI);

        schemas.forEach(schema -> {

            System.out.println("Schema: " + schema.name());

            schema.fields().forEach((name, field) -> {
                System.out.println("  Field: " + name);
                System.out.println("    Type: " + field.type());
                System.out.println("    Required: " + field.required());
                System.out.println("    MinLength: " + field.minLength());
                System.out.println("    MaxLength: " + field.maxLength());
                System.out.println("    Minimum: " + field.minimum());
                System.out.println("    Maximum: " + field.maximum());
                System.out.println("    Enum: " + field.enumValues());
                System.out.println("    Format: " + field.format());
                System.out.println("    IsArray: " + field.isArray());
                System.out.println("    ArrayItemType: " + field.arrayItemType());
                System.out.println("    MinItems: " + field.minItems());
                System.out.println("    MaxItems: " + field.maxItems());
            });

            System.out.println("----------------------");
        });

        System.out.println("OpenAPI loaded successfully!");

        PayloadGenerator generator = new PayloadGenerator();

        schemas.forEach(schema -> {

            System.out.println("Schema: " + schema.name());

            var positive = generator.generatePositive(schema);
            var edge = generator.generateEdgeCases(schema);
            var negative = generator.generateNegativeCases(schema);

            System.out.println("Positive:");
            System.out.println(positive);

            System.out.println("Edge Cases:");
            edge.forEach(System.out::println);

            System.out.println("Negative Cases:");
            negative.forEach(System.out::println);

            System.out.println("====================================");
        });
    }
}
