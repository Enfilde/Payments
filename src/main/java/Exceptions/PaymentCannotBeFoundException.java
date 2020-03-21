package Exceptions;

public class PaymentCannotBeFoundException extends OrganizationAndPaymentsException {
    public PaymentCannotBeFoundException() {
        super("PaymentCannotBeFound");
    }
}
