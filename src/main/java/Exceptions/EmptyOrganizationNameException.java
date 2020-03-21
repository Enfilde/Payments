package Exceptions;

public class EmptyOrganizationNameException extends OrganizationAndPaymentsException {
    public EmptyOrganizationNameException() {
        super("EmptyOrganizationName");
    }
}
