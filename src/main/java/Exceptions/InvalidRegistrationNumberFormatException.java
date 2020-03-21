package Exceptions;

public class InvalidRegistrationNumberFormatException extends OrganizationAndPaymentsException{
    public InvalidRegistrationNumberFormatException() {
        super("InvalidRegistrationNumberFormat");
    }
}
