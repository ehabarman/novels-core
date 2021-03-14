package com.itsmite.novels.core.graphql.instrument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import graphql.ExecutionResult;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.SimpleInstrumentationContext;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class RequestLoggingInstrumentation extends SimpleInstrumentation {

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();

    @Override
    public InstrumentationContext<ExecutionResult> beginExecution(InstrumentationExecutionParameters parameters) {
        long startMillis = System.currentTimeMillis();
        var executionId = parameters.getExecutionInput().getExecutionId();

        if (log.isInfoEnabled()) {
            log.info("GraphQL execution {} started", executionId);

            var query = parameters.getQuery();
            log.info("[{}] query: {}", executionId, query);
            if (parameters.getVariables() != null && !parameters.getVariables().isEmpty()) {
                log.info("[{}] variables: {}", executionId, parameters.getVariables());
            }
        }

        return new SimpleInstrumentationContext<>() {
            @SneakyThrows
            @Override
            public void onCompleted(ExecutionResult executionResult, Throwable t) {
                if (log.isInfoEnabled()) {
                    long endMillis = System.currentTimeMillis();

                    if (t != null) {
                        log.info("GraphQL execution {} failed: {}", executionId, t.getMessage(), t);
                    } else {
                        Map<String, Object> resultMap = executionResult.toSpecification();
                        String resultJSON = OBJECT_WRITER.writeValueAsString(resultMap);
                        log.info("[{}] completed in {}ms", executionId, endMillis - startMillis);
                        log.info("[{}] result:\n {}", executionId, resultJSON);
                    }
                }
            }
        };
    }
}