package Exceptions;

public class EmptyNameException extends OrganizationAndPaymentsException{
    public EmptyNameException() {
        super("EmptyOrganizationName");
    }
}
