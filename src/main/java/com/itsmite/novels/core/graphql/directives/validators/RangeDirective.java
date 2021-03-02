package com.itsmite.novels.core.graphql.directives.validators;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLDirectiveContainer;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

@Slf4j
public class RangeDirective extends ValidatorDirective implements SchemaDirectiveWiring {

    private static final String RANGE = "range";
    private static final String MIN   = "min";
    private static final String MAX   = "max";

    protected String getDirectiveName() {
        return RANGE;
    }

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        boolean applies = appliesTo(environment.getElement());
        if (applies) {
            replaceDataFetcher(environment);
        }
        return environment.getElement();
    }

    @Override
    protected List<GraphQLError> apply(GraphQLDirectiveContainer it, DataFetchingEnvironment env, String name, Object value) {
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

