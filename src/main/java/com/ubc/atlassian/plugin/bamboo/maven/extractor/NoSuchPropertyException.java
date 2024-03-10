package com.ubc.atlassian.plugin.bamboo.maven.extractor;

@SuppressWarnings("serial")
public class NoSuchPropertyException extends RuntimeException {

    public NoSuchPropertyException() {
        super();
    }

    public NoSuchPropertyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchPropertyException(String message) {
        super(message);
    }

    public NoSuchPropertyException(Throwable cause) {
        super(cause);
    }
}
