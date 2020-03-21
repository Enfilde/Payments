package Exceptions;

public class OrganizationCannotBeFoundException extends OrganizationAndPaymentsException {
    public OrganizationCannotBeFoundException() {
        super("OrganizationCannotBeFound");
    }
}
