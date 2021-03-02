package com.itsmite.novels.core.graphql.directives.validators;

import com.itsmite.novels.core.graphql.directives.DirectivesUtil;
import graphql.GraphQLError;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLDirectiveContainer;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ValidatorDirective {

    protected DataFetcher createDataFetcher(DataFetcher originalFetcher) {
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

    protected boolean appliesTo(GraphQLFieldDefinition fieldDefinition) {
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

    protected void replaceDataFetcher(SchemaDirectiveWiringEnvironment<? extends GraphQLDirectiveContainer> environment) {
        DataFetcher originalFetcher = environment.getFieldDataFetcher();
        DataFetcher newFetcher = createDataFetcher(originalFetcher);
        environment.getCodeRegistry().dataFetcher(environment.getFieldsContainer(), environment.getFieldDefinition(), newFetcher);
    }

    protected boolean appliesTo(GraphQLDirectiveContainer container) {
        return container.getDirective(getDirectiveName()) != null;
    }

    protected abstract List<GraphQLError> apply(GraphQLDirectiveContainer it, DataFetchingEnvironment env, String name, Object value);

    protected abstract String getDirectiveName();
}
