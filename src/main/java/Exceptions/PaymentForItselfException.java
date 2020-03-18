package Exceptions;

public class PaymentForItselfException extends OrganizationAndPaymentsException{
    public PaymentForItselfException() {
        super("PaymentForItself");
    }
}
