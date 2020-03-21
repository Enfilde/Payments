package Exceptions;

public class InvalidPaymentTimeException extends OrganizationAndPaymentsException {
    public InvalidPaymentTimeException() {
        super("InvalidPaymentTime");
    }
}
