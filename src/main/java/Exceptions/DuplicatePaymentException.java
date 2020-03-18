package Exceptions;

public class DuplicatePaymentException extends OrganizationAndPaymentsException {
    public DuplicatePaymentException() {
        super("DuplicatePayment");
    }

}
