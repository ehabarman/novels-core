package com.itsmite.novels.core.errors.exceptions;

import com.itsmite.novels.core.util.StringUtil;

/**
 * Exception indicates the resource with the given parameter value is already used/created
 *
 * @author ehab
 */
public class AlreadyUsedException extends RuntimeException {

    public AlreadyUsedException(String parameterName, String parameterValue) {
        super(generateMessage(parameterName, parameterValue));
    }

    private static String generateMessage(String parameterName, String parameterValue) {
        return StringUtil.appendAll(parameterName, ": '", parameterValue, "' is already used");
    }
}

