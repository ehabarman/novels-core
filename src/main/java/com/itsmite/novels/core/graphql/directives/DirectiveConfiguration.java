package com.itsmite.novels.core.graphql.directives;

import com.itsmite.novels.core.graphql.directives.validators.LengthDirective;
import com.itsmite.novels.core.graphql.directives.validators.RangeDirective;
import graphql.schema.idl.SchemaDirectiveWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectiveConfiguration {

    @Bean
    public SchemaDirectiveWiring lengthDirectiveWiring() {
        return new LengthDirective();
    }

    @Bean
    public SchemaDirectiveWiring rangeDirectiveWiring() {
        return new RangeDirective();
    }
}
