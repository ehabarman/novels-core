package com.itsmite.novels.core.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@EnableOpenApi
@Configuration
public class SpringFoxSwaggerConfig {

    private static final String CONTROLLERS_ENDPOINTS_ANT = "/api/**";
    private static final String ENTITIES_ENDPOINTS_REGEX  = "^\\/(?!(api|error|profile)).*";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
            .apiInfo(apiInfo())
            .securitySchemes(Collections.singletonList(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.ant(CONTROLLERS_ENDPOINTS_ANT))
            .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Novel Core REST APIs", null, "1.0", null, null, null, null, Collections.emptyList());
    }
}