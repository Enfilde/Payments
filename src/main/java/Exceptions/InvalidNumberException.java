package Exceptions;

public class InvalidNumberException extends OrganizationAndPaymentsException {
    public InvalidNumberException() {
        super("InvalidRegistrationNumberFormat");
    }
}
