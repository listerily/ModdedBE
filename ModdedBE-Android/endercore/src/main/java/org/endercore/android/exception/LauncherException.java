package org.endercore.android.exception;

public class LauncherException extends Exception {
    public LauncherException(String message) {
        super(message);
    }

    public LauncherException(String message, Error cause) {
        super(message, cause);
    }

    public LauncherException(String message, Exception cause) {
        super(message, cause);
    }

    public LauncherException(String message, Throwable cause) {
        super(message, cause);
    }
}
