package com.itsmite.novels.core.graphql.errors;

import graphql.ErrorClassification;

public enum GraphqlErrorTypes implements ErrorClassification {
    InvalidSyntax,
    ValidationError,
    DataFetchingException,
    NullValueInNonNullableField,
    OperationNotSupported,
    ExecutionAborted,
    UnauthorizedMutation
}
