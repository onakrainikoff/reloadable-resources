package ru.on8off.reloadable.resources.core;

public class ReloadableException extends RuntimeException{
    public ReloadableException(String message) {
        super(message);
    }

    public ReloadableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReloadableException(Throwable cause) {
        super(cause);
    }
}
