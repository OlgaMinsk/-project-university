package DTO;

public class DTOException extends Exception {
    public DTOException() {
        super();
    }

    public DTOException(String message) {
        super(message);
    }

    public DTOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DTOException(Throwable cause) {
        super(cause);
    }

    public DTOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

