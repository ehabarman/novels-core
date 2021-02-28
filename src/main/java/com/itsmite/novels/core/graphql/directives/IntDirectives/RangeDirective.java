package com.itsmite.novels.core.graphql.directives.IntDirectives;

import com.itsmite.novels.core.graphql.directives.DirectivesUtil;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

@Slf4j
public class RangeDirective implements SchemaDirectiveWiring {

    private static final String RANGE = "range";
    private static final String MIN   = "min";
    private static final String MAX   = "max";

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        boolean applies = appliesTo(environment.getElement());

        if (applies) {
            replaceDataFetcher(environment);
        }
        return environment.getElement();
    }

    private boolean appliesTo(GraphQLFieldDefinition fieldDefinition) {
        return fieldDefinition.getArguments().stream().anyMatch(it -> {
            if (appliesTo(it)) {
                return true;
            }
            GraphQLInputType unwrappedInputType = DirectivesUtil.unwrapNonNull(it.getType());
            if (unwrappedInputType instanceof GraphQLInputObjectType) {
                GraphQLInputObjectType inputObjType = (GraphQLInputObjectType)unwrappedInputType;
                return inputObjType.getFieldDefinitions().stream().anyMatch(this::appliesTo);
            }
            return false;
        });
    }

    private boolean appliesTo(GraphQLDirectiveContainer container) {
        return container.getDirective(RANGE) != null;
    }

    private void replaceDataFetcher(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        DataFetcher originalFetcher = environment.getFieldDataFetcher();
        DataFetcher newFetcher = createDataFetcher(originalFetcher);
        environment.getCodeRegistry().dataFetcher(environment.getFieldsContainer(), environment.getFieldDefinition(), newFetcher);
    }

    private DataFetcher createDataFetcher(DataFetcher originalFetcher) {
        return (env) -> {
            List<GraphQLError> errors = new ArrayList<>();
            env.getFieldDefinition().getArguments().forEach(it -> {
                if (appliesTo(it)) {
                    errors.addAll(apply(it, env, it.getName(), env.getArgument(it.getName())));
                }

                GraphQLInputType unwrappedInputType = DirectivesUtil.unwrapNonNull(it.getType());
                if (unwrappedInputType instanceof GraphQLInputObjectType) {
                    GraphQLInputObjectType inputObjType = (GraphQLInputObjectType)unwrappedInputType;
                    inputObjType.getFieldDefinitions().stream().filter(this::appliesTo).forEach(io -> {
                        Map<String, Object> value = env.getArgument(it.getName());
                        errors.addAll(apply(io, env, io.getName(), value.get(io.getName())));
                    });
                }
            });

            Object returnValue = originalFetcher.get(env);
            if (errors.isEmpty()) {
                return returnValue;
            }
            return DirectivesUtil.mkDFRFromFetchedResult(errors, returnValue);
        };
    }

    private List<GraphQLError> apply(GraphQLDirectiveContainer it, DataFetchingEnvironment env, String name, Object value) {
        if (value == null) {
            return emptyList();
        }
        if (!(value instanceof Integer)) {
            String errorMessage = String.format("Directive '%S' is used with integer type only", RANGE);
            log.warn(errorMessage);
            return singletonList(GraphqlErrorBuilder.newError(env).message(errorMessage).build());
        }
        GraphQLDirective directive = it.getDirective(RANGE);
        int intValue = (int)value;
        int min = ofNullable(directive.getArgument(MIN)).map(GraphQLArgument::getValue).map(val -> (int)val).orElse(Integer.MIN_VALUE);
        int max = ofNullable(directive.getArgument(MAX)).map(GraphQLArgument::getValue).map(val -> (int)val).orElse(Integer.MAX_VALUE);
        if (intValue < min || intValue > max) {
            String errorMessage = String.format("Argument(`%s`) is out of range(value=%s). The range is %s to %s.", name, value, min, max);
            log.warn(errorMessage);
            return singletonList(GraphqlErrorBuilder.newError(env).message(errorMessage).build());
        }
        return emptyList();
    }
}

