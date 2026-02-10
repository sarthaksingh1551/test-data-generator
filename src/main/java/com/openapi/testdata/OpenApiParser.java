package com.openapi.testdata;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

public class OpenApiParser {
    public OpenAPI parse(String content) {

        SwaggerParseResult result = new OpenAPIV3Parser()
                .readContents(content, null, null);

        if (result.getMessages() != null && !result.getMessages().isEmpty()) {
            System.out.println("Parsing Errors:");
            result.getMessages().forEach(System.out::println);
        }

        return result.getOpenAPI();
    }
}