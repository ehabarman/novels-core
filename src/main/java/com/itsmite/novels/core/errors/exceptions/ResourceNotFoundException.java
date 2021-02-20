package com.itsmite.novels.core.errors.exceptions;

import com.itsmite.novels.core.util.StringUtil;

/**
 * Exception triggered when a requested resource doesn't exist
 *
 * @author ehab
 */
public class ResourceNotFoundException extends RuntimeException  {

    public ResourceNotFoundException(String resourceName) {
        super(generateMessage(resourceName));
    }

    private static String generateMessage(String resourceName) {
        return StringUtil.appendAll(resourceName, " not found");
    }
}
