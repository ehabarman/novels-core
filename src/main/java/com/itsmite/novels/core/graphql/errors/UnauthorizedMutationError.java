package com.itsmite.novels.core.graphql.errors;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.ArrayList;
import java.util.List;

public class UnauthorizedMutationError extends RuntimeException implements GraphQLError {

    public UnauthorizedMutationError(String mutationName) {
        super(String.format("Unauthorized mutation: %s", mutationName));
    }

    @Override
    public List<SourceLocation> getLocations() {
        return new ArrayList<>();
    }

    @Override
    public ErrorClassification getErrorType() {
        return GraphqlErrorTypes.UnauthorizedMutation;
    }
}