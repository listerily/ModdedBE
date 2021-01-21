package org.endercore.android.exception;

public class NModException extends Exception {

    public NModException(String message) {
        super(message);
    }

    public NModException(String message, Error cause) {
        super(message, cause);
    }

    public NModException(String message, Exception cause) {
        super(message, cause);
    }

    public NModException(String message, Throwable t) {
        super(message, t);
    }
}
