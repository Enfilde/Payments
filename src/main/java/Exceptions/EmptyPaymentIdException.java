package Exceptions;

public class EmptyPaymentIdException extends OrganizationAndPaymentsException {
    public EmptyPaymentIdException(){
        super("EmptyPaymentId");
    }
}
