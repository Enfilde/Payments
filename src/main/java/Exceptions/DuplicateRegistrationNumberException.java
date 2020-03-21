package Exceptions;

public class DuplicateRegistrationNumberException extends OrganizationAndPaymentsException {
    public DuplicateRegistrationNumberException() {
        super("DuplicateRegistrationNumber");
    }
}
