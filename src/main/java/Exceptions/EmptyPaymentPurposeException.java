package Exceptions;

public class EmptyPaymentPurposeException extends OrganizationAndPaymentsException {
    public EmptyPaymentPurposeException() {
        super("EmptyPaymentPurpose");
    }
}
