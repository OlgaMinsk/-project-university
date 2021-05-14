package DAO;

public class DAONoSuchPageException extends DAO.DAOException {
    public DAONoSuchPageException(){
        super();
    }
    public DAONoSuchPageException(String message) {
        super(message);
    }

    public DAONoSuchPageException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAONoSuchPageException(Throwable cause) {
        super(cause);
    }

    public DAONoSuchPageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
