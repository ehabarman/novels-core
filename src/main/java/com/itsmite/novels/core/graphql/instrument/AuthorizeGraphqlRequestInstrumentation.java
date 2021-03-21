package com.itsmite.novels.core.graphql.instrument;

import com.itsmite.novels.core.graphql.errors.UnauthorizedMutationError;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import graphql.PublicApi;
import graphql.execution.AbortExecutionException;
import graphql.execution.instrumentation.InstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecuteOperationParameters;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.Selection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@PublicApi
public class AuthorizeGraphqlRequestInstrumentation extends SimpleInstrumentation {

    private static final Set<String> MUTATION_WHITELIST;

    static {
        MUTATION_WHITELIST = new HashSet<>() {{
            add("login");
            add("register");
        }};
    }

    @Override
    public InstrumentationContext<ExecutionResult> beginExecuteOperation(InstrumentationExecuteOperationParameters parameters) {
        OperationDefinition operationDefinition = parameters.getExecutionContext().getOperationDefinition();
        List<Selection> selections = operationDefinition.getSelectionSet().getSelections();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GraphQLError> errors = new ArrayList<>();
        if (isMutation(operationDefinition) && isAnonymousUser(authentication)) {
            selections.stream().map(selection -> ((Field)selection).getName())
                      .filter(fieldName -> !isWhitelistedMutation(fieldName))
                      .forEach(fieldName -> errors.add(new UnauthorizedMutationError(fieldName)));
        }
        if (!errors.isEmpty()) {
            throw new AbortExecutionException(errors);
        }
        return super.beginExecuteOperation(parameters);
    }

    private boolean isMutation(OperationDefinition operationDefinition) {
        return OperationDefinition.Operation.MUTATION.equals(operationDefinition.getOperation());
    }

    private boolean isAnonymousUser(Authentication authentication) {
        return "anonymousUser".equals(authentication.getPrincipal());
    }

    private boolean isWhitelistedMutation(String mutationName) {
        return MUTATION_WHITELIST.contains(mutationName);
    }
}
