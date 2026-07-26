package com.onlineexam.dao;

/**
 * Unchecked exception that wraps low-level {@link java.sql.SQLException}s so the
 * controller layer can let genuine data-access failures propagate to the
 * configured error page instead of silently swallowing them.
 */
public class DataAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
