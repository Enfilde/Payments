package Exceptions;

public class EmptyIdException extends OrganizationAndPaymentsException{
    public EmptyIdException() {
        super("EmptyPaymentId");
    }
}
