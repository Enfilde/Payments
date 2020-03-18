package Exceptions;

public class OrganizationNotFoundException extends OrganizationAndPaymentsException {
    public OrganizationNotFoundException() {
        super("OrganizationCannotBeFound");
    }
}
