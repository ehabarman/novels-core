package com.itsmite.novels.core.graphql;

import com.itsmite.novels.core.graphql.instrument.AuthorizeGraphqlRequestInstrumentation;
import com.itsmite.novels.core.graphql.instrument.RequestLoggingInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class InstrumentationService {

    /**
     * Return all instrumentations as a bean.
     * The result will be used in class {@link graphql.kickstart.spring.web.boot.GraphQLWebAutoConfiguration}.
     */
    @Bean
    public List<Instrumentation> instrumentations() {
        // Note: Due to a bug in GraphQLWebAutoConfiguration, the returned list has to be modifiable (it will be sorted)
        return new ArrayList<>(List.of(
            new AuthorizeGraphqlRequestInstrumentation(),
            new RequestLoggingInstrumentation()
        ));
    }
}