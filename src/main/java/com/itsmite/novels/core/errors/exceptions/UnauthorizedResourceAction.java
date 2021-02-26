package com.itsmite.novels.core.errors.exceptions;

public class UnauthorizedResourceAction extends RuntimeException {

    public UnauthorizedResourceAction(String resourceType, String resourceId) {
        super(String.format("Unauthorized action to resource %s(id=%s)", resourceType, resourceId));
    }
}