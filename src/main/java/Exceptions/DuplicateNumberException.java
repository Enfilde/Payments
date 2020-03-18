package Exceptions;

public class DuplicateNumberException extends OrganizationAndPaymentsException {
    public DuplicateNumberException() {
        super("DuplicateRegistrationNumber");
    }
}
