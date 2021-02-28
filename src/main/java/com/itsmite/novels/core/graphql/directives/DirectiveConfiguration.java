package com.itsmite.novels.core.graphql.directives;

import com.itsmite.novels.core.graphql.directives.IntDirectives.RangeDirective;
import graphql.schema.idl.SchemaDirectiveWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectiveConfiguration {

    @Bean
    public SchemaDirectiveWiring directiveWiring() {
        return new RangeDirective();
    }
}
