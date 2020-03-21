package Exceptions;

public class DuplicateOrganizationException extends OrganizationAndPaymentsException {
    public DuplicateOrganizationException() {
        super("DuplicateOrganization");
    }
}
