package Exceptions;

public class OrganizationAndPaymentsException extends Exception{
    public OrganizationAndPaymentsException() {
        super();
    }

    public OrganizationAndPaymentsException(String message) {
        super(message);
    }

    public OrganizationAndPaymentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrganizationAndPaymentsException(Throwable cause) {
        super(cause);
    }

    protected OrganizationAndPaymentsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}