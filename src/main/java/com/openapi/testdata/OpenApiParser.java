package com.openapi.testdata;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;

public class OpenApiParser {

    public OpenAPI parse(String filePath) {
        return new OpenAPIV3Parser().read(filePath);
    }
}