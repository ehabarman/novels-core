package com.itsmite.novels.core.graphql.directives.validators;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

@Slf4j
public class LengthDirective extends ValidatorDirective implements SchemaDirectiveWiring {

    private static final String LENGTH               = "length";
    private static final String MIN                  = "min";
    private static final String MAX                  = "max";
    private static final String KEEP_TRAILING_SPACES = "keepTrailingSpaces";
    private static final String KEEP_LEADING_SPACES  = "keepLeadingSpaces";

    protected String getDirectiveName() {
        return LENGTH;
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
        if (!(value instanceof String)) {
            String errorMessage = String.format("Directive '%S' is used with string type only", LENGTH);
            log.warn(errorMessage);
            return singletonList(GraphqlErrorBuilder.newError(env).message(errorMessage).build());
        }

        String stringValue = value.toString();
        GraphQLDirective directive = it.getDirective(LENGTH);
        if (!(Boolean.parseBoolean(String.valueOf(directive.getArgument(KEEP_LEADING_SPACES))))) {
            stringValue = stringValue.stripLeading();
        }
        if (!(Boolean.parseBoolean(String.valueOf(directive.getArgument(KEEP_TRAILING_SPACES))))) {
            stringValue = stringValue.stripTrailing();
        }
        int length = stringValue.length();
        int min = ofNullable(directive.getArgument(MIN)).map(GraphQLArgument::getValue).map(val -> (int)val).orElse(Integer.MIN_VALUE);
        int max = ofNullable(directive.getArgument(MAX)).map(GraphQLArgument::getValue).map(val -> (int)val).orElse(Integer.MAX_VALUE);
        if (length < min || length > max) {
            String errorMessage = String.format("Argument(`%s`) is out of range(value=%s). The range is %s to %s.", name, value, min, max);
            log.warn(errorMessage);
            return singletonList(GraphqlErrorBuilder.newError(env).message(errorMessage).build());
        }
        return emptyList();
    }
}

