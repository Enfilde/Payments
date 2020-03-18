package Exceptions;

public class NotFoundPaymentException extends OrganizationAndPaymentsException{
    public NotFoundPaymentException() {
        super("PaymentCannotBeFound");
    }
}
