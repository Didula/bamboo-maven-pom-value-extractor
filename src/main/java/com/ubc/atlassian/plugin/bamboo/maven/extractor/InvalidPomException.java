package com.ubc.atlassian.plugin.bamboo.maven.extractor;

/**
 * @author David Ehringer
 */
@SuppressWarnings("serial")
public class InvalidPomException extends RuntimeException {

    public InvalidPomException() {
    }

    public InvalidPomException(String message, Throwable t) {
        super(message, t);
    }

    public InvalidPomException(String message) {
        super(message);
    }

    public InvalidPomException(Throwable t) {
        super(t);
    }
}
