package Exceptions;

public class EmptyPaymentException extends OrganizationAndPaymentsException {
    public EmptyPaymentException() {
        super("EmptyPaymentPurpose");
    }
}
